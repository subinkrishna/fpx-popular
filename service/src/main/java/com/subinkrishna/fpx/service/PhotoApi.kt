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
package com.subinkrishna.fpx.service

import com.subinkrishna.fpx.service.model.PhotoStream
import com.subinkrishna.fpx.service.model.Photo
import io.reactivex.Single

/** Api interface definition */
interface PhotoApi {

    /** Returns the page of photo feed for the given feature */
    fun photos(feature: String, page: Int, resultsPerPage: Int): Single<PhotoStream>

    /** Returns the photo by id */
    fun photoById(id: String): Single<Photo>
}