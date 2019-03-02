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
package com.subinkrishna.fpx.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Type to represent a photo item from a stream that contains
 * dimensions, EXIF, user, location & image details
 */
@Parcelize
data class Photo(
    val id: Long,
    val name: String,
    val description: String?,
    val width: Int,
    val height: Int,
    val images: List<Image>,
    @SerializedName("taken_at") val takenAt: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("times_viewed") val viewCount: Int,
    @SerializedName("votes_count") val voteCount: Int,
    @SerializedName("comments_count") val commentCount: Int,
    val rating: Float,
    val user: User,
    val camera: String?,
    val lens: String?,
    @SerializedName("focal_length") val focalLength: String?,
    val iso: String?,
    val aperture: String?,
    @SerializedName("shutter_speed") val shutterSpeed: String?,
    val location: String?,
    val latitude: Float?,
    val longitude: Float?
): Parcelable

