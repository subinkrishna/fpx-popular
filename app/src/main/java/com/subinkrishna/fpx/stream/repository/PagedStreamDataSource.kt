/**
 * Copyright (C) 2019 Subinkrishna Gopi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.subinkrishna.fpx.stream.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.subinkrishna.fpx.ktx.plusAssign
import com.subinkrishna.fpx.service.PhotoApi
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.model.NetworkState
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * A paged DataSource that fetches the photos from the stream
 * page by page.
 *
 * Note:
 * This is not the most efficient solution for pagination
 * since PageKeyedDataSource doesn't seem to support page dropping yet.
 * And hence holds the entire data set in memory all the time.
 * The ideal solution will be to use a local SQLite table as an
 * intermediate cache and let the UI to consume the data only from it.
 */
class PagedStreamDataSource(
    private val api: PhotoApi,
    private val feature: String,
    private val disposable: CompositeDisposable
) : PageKeyedDataSource<Int, Photo>() {

    /** Data source factory */
    class Factory(
        private val api: PhotoApi,
        private val feature: String,
        private val disposable: CompositeDisposable
    ) : DataSource.Factory<Int, Photo>() {
        // Holds the most recent instance of the data source
        // This instance is used to retry/invalidation
        val dataSourceLive by lazy {
            MutableLiveData<PagedStreamDataSource>()
        }

        override fun create(): DataSource<Int, Photo> {
            Timber.d("Create data source: $feature")
            return PagedStreamDataSource(api, feature, disposable).also {
                dataSourceLive.postValue(it)
            }
        }
    }

    // Network state live data
    val networkStateLive = MutableLiveData<NetworkState>()

    // Retry function
    private var retry: (() -> Unit)? = null

    /** Loads the initial page */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        val size = params.requestedLoadSize
        try {
            networkStateLive.postValue(NetworkState.Loading(1))
            // Synchronously loading the first page
            val stream = api.photos(feature, 1, size).blockingGet()
            // Notify
            callback.onResult(stream.photos, 0, 2)

            networkStateLive.postValue(NetworkState.Ready(1))
        } catch (t: Throwable) {
            Timber.e("Error! $t")
            retry = {
                loadInitial(params, callback)
            }
            networkStateLive.postValue(NetworkState.Error(1))
        }
    }

    /** Loads subsequent pages as needed */
    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Photo>
    ) {
        val pageNumber = params.key
        val size = params.requestedLoadSize

        // This needs to happen asynchronously
        networkStateLive.postValue(NetworkState.Loading(pageNumber))
        disposable += api.photos(feature, pageNumber, size)
            .subscribeOn(Schedulers.io())
            .subscribe({
                // Check if the stream has more photos
                val hasMorePhotos = it.totalItems > pageNumber * size
                // Notify
                callback.onResult(
                    it.photos,
                    if (hasMorePhotos) pageNumber + 1 else null)
                networkStateLive.postValue(NetworkState.Ready(pageNumber))
            }, {
                Timber.e("Error! ${it.message}")
                retry = {
                    loadAfter(params, callback)
                }
                networkStateLive.postValue(NetworkState.Error(pageNumber))
            })
    }

    /**
     * Fetches pages "before" current position
     *
     * Note:
     * Currently unused since the stream always starts loading from first page
     */
    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Photo>
    ) = Unit

    /** Execute retry action asynchronously, if any */
    fun retry() {
        val action = retry
        retry = null
        if (action != null) {
            disposable += Completable.fromAction(action)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }
}