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

import com.subinkrishna.fpx.service.model.Photo
import java.text.SimpleDateFormat
import java.util.*

/** Returns the small image from the image set */
val Photo.smallImage: String?
    get() = images.find { it.size == 21 } ?.url ?: images.getOrNull(0)?.url

/** Returns the large image from the image set */
val Photo.largeImage: String?
    get() = images.find { it.size == 1080 } ?.url ?: smallImage

/** Checks if the Photo instance has location data */
val Photo.hasLocation: Boolean
    get() = !location.isNullOrBlank() || (latitude != null && longitude != null)

/**
 * Returns a formatted date to display. This implementation looks for "taken_at"
 * and fall backs to "created_at".
 */
val Photo.displayDate: String
    get() {
        val date = takenAt ?: createdAt
        return try {
            date?.run {
                val inputPattern = "yyyy-MM-dd'T'HH:mm:ss"
                val outputPattern = "EEEE, MMMM d yyyy"
                val sdf = SimpleDateFormat(inputPattern, Locale.getDefault())
                val parsedDate = sdf.parse(this)
                sdf.applyPattern(outputPattern)
                sdf.format(parsedDate)
            }.orEmpty()
        } catch (t: Throwable) {
            ""
        }
    }

/** Returns formatted camera & setting details */
val Photo.cameraDetails: String
    get() {
        val separator = " "
        val cameraLens = StringBuilder()
            .append(camera.emptyIfNull())
            .appendIfNotEmpty(separator)
            .append(lens.emptyIfNull())

        val settings = StringBuilder()
            .append(aperture.emptyIfNull(prefix = "f/"))
            .appendIfNotEmpty(separator)
            .append(shutterSpeed.emptyIfNull())
            .appendIfNotEmpty(separator)
            .append(focalLength.emptyIfNull(suffix = "mm"))
            .appendIfNotEmpty(separator)
            .append(iso.emptyIfNull(prefix = "ISO "))

        return StringBuilder().apply {
            append(cameraLens.toString().trim())
            if (!settings.isNullOrBlank()) appendIfNotEmpty("\n")
            append(settings.toString().trim())
        }.toString()
    }

/** Returns formatted location details */
val Photo.locationDetails: String
    get() {
        return StringBuilder().apply {
            append(location.emptyIfNull())
            appendIfNotEmpty("\n")
            val latLong = if (latitude != null && longitude != null) {
                "$latitude, $longitude"
            } else ""
            append(latLong)
        }.toString().trim()
    }

private fun String?.emptyIfNull(
    prefix: String = "",
    suffix: String = ""
): String {
    return if (this.isNullOrBlank()) "" else "$prefix${this.trim()}$suffix"
}

private fun StringBuilder.appendIfNotEmpty(text: CharSequence): StringBuilder {
    if (length > 0 && !endsWith(text))
        append(text)
    return this
}
