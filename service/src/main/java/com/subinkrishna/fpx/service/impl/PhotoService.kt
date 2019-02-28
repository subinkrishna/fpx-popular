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

import com.subinkrishna.fpx.service.model.PhotoStream
import com.subinkrishna.fpx.service.model.Photo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** A Retrofit service definition for photo service */
interface PhotoService {

    @GET("photos")
    fun photos(
        @Query("feature") feature: String,
        @Query("page") page: Int,
        @Query("rpp") resultsPerPage: Int,
        @Query("image_size") imageSize: String
    ): Single<PhotoStream>

    @GET("photos/{id}")
    fun photoById(
        @Path("id") id: String,
        @Query("image_size") imageSize: String
    ): Single<Photo>
}