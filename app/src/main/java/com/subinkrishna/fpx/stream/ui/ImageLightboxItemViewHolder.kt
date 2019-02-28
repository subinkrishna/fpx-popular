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

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.subinkrishna.fpx.service.model.Photo

/**
 * ViewHolder for image items in the photo stream
 */
class ImageLightboxItemViewHolder(val v: ImageLightboxView) : RecyclerView.ViewHolder(v) {

    companion object {
        fun create(parent: ViewGroup): ImageLightboxItemViewHolder {
            val v = ImageLightboxView(parent.context)
            v.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            return ImageLightboxItemViewHolder(v)
        }
    }

    fun bind(item: Photo?) {
        v.bind(item)
    }
}