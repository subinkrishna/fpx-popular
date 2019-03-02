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
package com.subinkrishna.fpx.stream.model

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.subinkrishna.fpx.ktx.plusAssign
import com.subinkrishna.fpx.service.PhotoApi
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.repository.PagedStreamDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * [AndroidViewModel] implementation for photo stream. This ViewModel is
 * responsible for fetching, paginating & retaining the stream data throughout
 * the lifespan of the stream "screen"
 */
class PhotoStreamViewModel(
    private val app: Application,
    private val api: PhotoApi,
    private val feature: String
) : AndroidViewModel(app) {

    /** ViewModel factory */
    class Factory(
        private val app: Application,
        private val api: PhotoApi,
        private val feature: String = "popular"
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PhotoStreamViewModel(app, api, feature) as T
        }
    }

    private val disposables = CompositeDisposable()
    private val viewStateLive = MutableLiveData<ViewState>()
    private val dataSourceFactory = PagedStreamDataSource.Factory(
        api, feature, disposables)

    // LiveData that holds the network state (any page
    val networkState: LiveData<NetworkState> = switchMap(dataSourceFactory.dataSourceLive) {
        it.networkStateLive
    }

    // Page configurations
    private val pageSize = 40
    private val pagingConfig by lazy {
        PagedList.Config.Builder().apply {
            setInitialLoadSizeHint(pageSize)
            setPageSize(pageSize)
            setPrefetchDistance(pageSize)
        }.build()
    }

    init {
        // Create PagedList observable from the data source
        val stream = RxPagedListBuilder<Int, Photo>(
            dataSourceFactory, pagingConfig
        ).buildObservable()

        // Subscribe to the stream and update the LiveData
        disposables += stream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val current = viewStateLive.value ?: ViewState()
                viewStateLive.value = current.copy(isLoading = false, items = it)
            }, {
                Timber.e("Error! ${it.message}")
                // todo: handle error
            })

        // Initial view state
        viewStateLive.value = ViewState(isLoading = true)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /** Returns an immutable version of ViewState LiveData */
    fun viewStateLive(): LiveData<ViewState> = viewStateLive

    /** Invalidates the current data set and reloads it */
    fun refresh() {
        dataSourceFactory.dataSourceLive.value?.invalidate()
    }

    /** Trigger the retry action, if available */
    fun retry() {
        dataSourceFactory.dataSourceLive.value?.retry()
    }

}