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
@file:Suppress("NOTHING_TO_INLINE")

package com.subinkrishna.fpx.ktx

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso

/**
 * Loads an image from the given URL to an [ImageView] using [Glide]
 *
 * @param url
 * @param centerCrop
 * @param placeHolderRes
 */
fun ImageView.setImageUrl(
        url: String?,
        centerCrop: Boolean = false,
        @DrawableRes placeHolderRes: Int = -1
) {
    val picasso = Picasso.get()
    // Enable indicators in debug mode
    if (BuildConfig.DEBUG) picasso.setIndicatorsEnabled(true)
    // Construct the request
    val request = picasso.load(url)
    request.apply {
        if (placeHolderRes != -1) placeholder(placeHolderRes)
        if (centerCrop) centerCrop() else centerInside()
        fit()
    }
    request.into(this)
}