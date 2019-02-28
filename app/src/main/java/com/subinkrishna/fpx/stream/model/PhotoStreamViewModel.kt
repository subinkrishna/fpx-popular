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
import com.subinkrishna.fpx.ktx.plusAssign
import com.subinkrishna.fpx.service.PhotoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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

    private val disposables = CompositeDisposable()
    private val stateLive = MutableLiveData<ViewState>()

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

    init {
        stateLive.value = ViewState()
        disposables += api.photos(feature, 1, 20)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val currentState = stateLive.value ?: ViewState()
                    stateLive.value = currentState.copy(
                        isLoading = false,
                        error = null,
                        items = it.photos
                    )
                },
                {

                })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun viewStateLive(): LiveData<ViewState> = stateLive

}