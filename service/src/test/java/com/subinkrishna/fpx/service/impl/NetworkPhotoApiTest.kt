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

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.subinkrishna.fpx.service.contentsAsString
import com.subinkrishna.fpx.service.model.PhotoStream
import org.junit.Test

/**
 * Unit tests to cover API, parser & data classes
 */
class NetworkPhotoApiTest {

    @Test
    fun testDataClasses() {
        val json = "popular-small.json".contentsAsString()
        val stream = Gson().fromJson(json, PhotoStream::class.java)

        // Test meta data
        assertThat(stream.currentPage).isEqualTo(1)
        assertThat(stream.totalPages).isEqualTo(1000)
        assertThat(stream.totalItems).isEqualTo(49400)
        assertThat(stream.photos.size).isEqualTo(1)

        // Test photo
        val firstPhoto = stream.photos[0]
        assertThat(firstPhoto.id).isEqualTo("296328931")
        assertThat(firstPhoto.name).isEqualTo("Natasha")
        assertThat(firstPhoto.shutterSpeed).isEqualTo("1/320")
        assertThat(firstPhoto.rating).isEqualTo(99.8f)

        // Images
        assertThat(firstPhoto.images.size).isEqualTo(2)
        assertThat(firstPhoto.images[0].size).isEqualTo(21)

        // User
        assertThat(firstPhoto.user.id).isEqualTo("777395")
        assertThat(firstPhoto.user.username).isEqualTo("SeanArcher")
    }
}