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
package com.subinkrishna.fpx.service.impl

import com.subinkrishna.fpx.service.BuildConfig
import com.subinkrishna.fpx.service.PhotoApi
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.service.model.PhotoStream
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit based implementation of Photo API
 */
class NetworkPhotoApi : PhotoApi {

    companion object {

        // Base URL
        private const val BASE_URL = "https://api.500px.com/v1/"

        // Defaults
        // todo: may need to pick the image sizes based on device's display
        private const val IMAGE_SIZES = "21,1080"

        // Params
        private const val QUERY_API_KEY = "consumer_key"

        // Client builder
        private val httpClientBuilder by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            return@lazy OkHttpClient.Builder().apply {
                // Log interceptor
                addInterceptor(loggingInterceptor)
                // Api key interceptor
                addInterceptor { chain ->
                    val request = chain.request()
                    val url = request.url().newBuilder()
                        .addQueryParameter(QUERY_API_KEY, BuildConfig.API_KEY)
                        .build()
                    val updatedRequest = request.newBuilder().url(url).build()
                    chain.proceed(updatedRequest)
                }

                // todo: add n/w caching
                // addInterceptor() // cache interceptor
                // cache()
            }
        }

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build()
        }

        private val service by lazy { retrofit.create(PhotoService::class.java) }
    }

    override fun photos(feature: String, page: Int, resultsPerPage: Int): Single<PhotoStream> {
        return service.photos(feature, page, resultsPerPage, IMAGE_SIZES)
    }

    override fun photoById(id: String): Single<Photo> {
        return service.photoById(id, IMAGE_SIZES)
    }
}

