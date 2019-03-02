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
package com.subinkrishna.fpx.di

import com.subinkrishna.fpx.service.PhotoApi
import com.subinkrishna.fpx.service.impl.NetworkPhotoApi
import timber.log.Timber

/** Service locator interface definition */
interface ServiceLocator {
    companion object {
        // Service locator instance
        private val instance: ServiceLocator by lazy { DefaultServiceLocator() }

        /** Returns the ServiceLocator instance */
        fun get(): ServiceLocator = instance
    }

    fun api(): PhotoApi
}

/** Default ServiceLocator implementation */
open class DefaultServiceLocator : ServiceLocator {

    private val api: PhotoApi by lazy {
        Timber.d("Creating API")
        NetworkPhotoApi()
    }

    override fun api(): PhotoApi = api
}