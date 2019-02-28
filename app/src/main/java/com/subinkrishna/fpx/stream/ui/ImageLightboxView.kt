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
package com.subinkrishna.fpx.stream.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.github.chrisbanes.photoview.PhotoView
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.ktx.setImageUrl
import com.subinkrishna.fpx.service.model.Photo

/**
 * Image lightbox view. This view contains a zoomable [ImageView],
 * user's avatar, username and image title
 */
class ImageLightboxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: PhotoView
    private val avatarView: ImageView
    private val usernameText: TextView
    private val titleText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_image_lightbox, this, true)
        imageView = findViewById<PhotoView>(R.id.image).apply {
            // todo: avoid intermediate zoom state?
            setScaleLevels(1f, 2f, 3f)
            setAllowParentInterceptOnEdge(true)
        }
        avatarView = findViewById(R.id.avatar)
        usernameText = findViewById(R.id.username)
        titleText = findViewById(R.id.title)
    }

    fun bind(item: Photo?) {
        // todo: pick the 1080 version
        val url = item?.images?.get(1)?.url
        val avatarUrl = item?.user?.avatar
        val username = item?.user?.username
        val title = item?.name

        imageView.setImageUrl(
            url = url, crossfade = true)
        avatarView.setImageUrl(
            url = avatarUrl,
            crossfade = true,
            centerCrop = true,
            placeHolderRes = R.drawable.placeholder_media_thumbnail)
        usernameText.text = username.orEmpty()
        usernameText.isVisible = !username.isNullOrBlank()
        titleText.text = title.orEmpty()
        titleText.isVisible = !title.isNullOrBlank()
    }

}