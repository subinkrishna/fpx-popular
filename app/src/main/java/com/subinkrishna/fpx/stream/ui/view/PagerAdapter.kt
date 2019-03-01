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
package com.subinkrishna.fpx.stream.ui.view

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.ui.vh.ImageLightboxItemViewHolder

/** Photo pager adapter */
class PagerAdapter : PagedListAdapter<Photo, ImageLightboxItemViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLightboxItemViewHolder {
        return ImageLightboxItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ImageLightboxItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position)?.id ?: -1L

    fun getItemAt(position: Int) = getItem(position)
}

/** Photo item diff */
// todo: move to a common class
private val DIFF = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }
}