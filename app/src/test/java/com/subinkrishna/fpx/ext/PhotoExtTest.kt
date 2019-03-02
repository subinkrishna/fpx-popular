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
package com.subinkrishna.fpx.ext

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.subinkrishna.fpx.contentsAsString
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.service.model.PhotoStream
import org.junit.Before
import org.junit.Test

/**
 * Simple unit tests to make sure that Photo extension
 * functions behave as expected
 */
class PhotoExtTest {

    lateinit var photo: Photo

    @Before
    fun init(){
        val json = "popular-small.json".contentsAsString()
        val stream = Gson().fromJson(json, PhotoStream::class.java)
        photo = stream.photos[0]
    }

    @Test
    fun testImageExtensions() {
        // Valid images
        assertThat(photo.smallImage).isEqualTo("https://drscdn.500px.org/photo/296328931/h%3D600_k%3D1_a%3D1/v2?client_application_id=8016&webp=true&sig=377b0552d440146b6e408fa8c91cec4bfacee3453245d6f68a1b126ccc9ee081")
        assertThat(photo.largeImage).isEqualTo("https://drscdn.500px.org/photo/296328931/m%3D1080_k%3D1_a%3D1/v2?client_application_id=8016&webp=true&sig=10deed272a9f8e88fe966bf814e1f06f3aa7cb98cc71788f735f9aaf2d2fb1e3")

        // Missing images
        // photo.largeImage should return smallImage if the 1080 image is missing
        val images = listOf(photo.images[0])
        val photoWithOneImage = photo.copy(images = images)
        assertThat(photoWithOneImage.smallImage).isEqualTo("https://drscdn.500px.org/photo/296328931/h%3D600_k%3D1_a%3D1/v2?client_application_id=8016&webp=true&sig=377b0552d440146b6e408fa8c91cec4bfacee3453245d6f68a1b126ccc9ee081")
        assertThat(photoWithOneImage.largeImage).isEqualTo(photoWithOneImage.smallImage)

        // No images
        val photoWithNoImages = photo.copy(images = emptyList())
        assertThat(photoWithNoImages.smallImage).isNull()
        assertThat(photoWithNoImages.largeImage).isNull()
    }

    @Test
    fun testLocationExtensions() {
        // With both location & lat-long
        val photoWithLoc = photo.copy(
            location = "location", latitude = 1.0f, longitude = -1.0f)
        assertThat(photoWithLoc.hasLocation).isTrue()
        assertThat(photoWithLoc.locationDetails).isEqualTo("location\n" +
                "1.0, -1.0")

        // With only location
        val photoWithOnlyLoc = photo.copy(
            location = "location", latitude = null, longitude = null)
        assertThat(photoWithOnlyLoc.hasLocation).isTrue()
        assertThat(photoWithOnlyLoc.locationDetails).isEqualTo("location")

        // With only lat-long
        val photoWithLatLong = photo.copy(
            location = "", latitude = 1.0f, longitude = -1.0f)
        assertThat(photoWithLatLong.hasLocation).isTrue()
        assertThat(photoWithLatLong.locationDetails).isEqualTo("1.0, -1.0")

        // With only lat or long
        val photoWithLat = photo.copy(
            location = "", latitude = 1.0f, longitude = null)
        assertThat(photoWithLat.hasLocation).isFalse()
        assertThat(photoWithLat.locationDetails).isEqualTo("")

        val photoWithLong = photo.copy(
            latitude = null, longitude = -1.0f)
        assertThat(photoWithLong.hasLocation).isFalse()
        assertThat(photoWithLong.locationDetails).isEqualTo("")

        // With no location
        val photoWithNoLoc = photo.copy(
            location = "", latitude = null, longitude = null)
        assertThat(photoWithNoLoc.hasLocation).isFalse()
        assertThat(photoWithNoLoc.locationDetails).isEqualTo("")
    }

    @Test
    fun testCameraExtensions() {
        // Will complete settings
        val photoWithAll = photo.copy(
            camera = "camera", lens = "lens 100mm",
            aperture = "1.8", shutterSpeed = "1/100", iso = "100", focalLength = "100"
        )
        assertThat(photoWithAll.cameraDetails)
            .isEqualTo("camera lens 100mm\n" +
                        "f/1.8 1/100 100mm ISO 100")

        // With incomplete
        val photoWithMissingSettings = photo.copy(
            camera = "camera", lens = "lens 100mm",
            aperture = "1.8", shutterSpeed = null, iso = null, focalLength = "100"
        )
        assertThat(photoWithMissingSettings.cameraDetails)
            .isEqualTo("camera lens 100mm\n" +
                        "f/1.8 100mm")

        val photoWithMissingLens = photo.copy(
            camera = "camera", lens = null,
            aperture = "1.8", shutterSpeed = null, iso = null, focalLength = "100"
        )
        assertThat(photoWithMissingLens.cameraDetails)
            .isEqualTo("camera\n" +
                    "f/1.8 100mm")

        // Camera with no camera info
        val photoWithNoCameraInfo = photo.copy(
            camera = "", lens = null,
            aperture = "", shutterSpeed = null, iso = null, focalLength = ""
        )
        assertThat(photoWithNoCameraInfo.cameraDetails).isEqualTo("")
    }

}