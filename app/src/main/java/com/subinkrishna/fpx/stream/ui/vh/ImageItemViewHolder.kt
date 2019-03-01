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
package com.subinkrishna.fpx.stream.ui.vh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.subinkrishna.aspect.AspectRatioImageView
import com.subinkrishna.aspect.AspectRatioLayout
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.ktx.setImageUrl
import com.subinkrishna.fpx.service.model.Photo

/**
 * ViewHolder for image items in the photo stream
 */
class ImageItemViewHolder(val v: AspectRatioImageView) : RecyclerView.ViewHolder(v) {

    companion object {
        /** Creates a new view holder instance */
        fun create(parent: ViewGroup): ImageItemViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_stream_grid_image_item, parent, false)
            return ImageItemViewHolder(v as AspectRatioImageView)
        }
    }

    fun bind(item: Photo?) {
        val url = item?.images?.get(0)?.url
        val aspectRatio = when (item) {
            null -> 1F
            else -> item.width.toFloat() / item.height.toFloat()
        }

        // Timber.d("bind: ${item?.width} x ${item?.height} = $aspectRatio, $url")

        v.apply {
            ratio(aspectRatio)
            lock(AspectRatioLayout.WIDTH)
            setImageUrl(
                url = url,
                placeHolderRes = R.drawable.placeholder_media_thumbnail)
        }
    }
}