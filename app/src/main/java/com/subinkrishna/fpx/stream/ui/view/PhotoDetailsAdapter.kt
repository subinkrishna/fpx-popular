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
import androidx.recyclerview.widget.RecyclerView
import com.subinkrishna.fpx.ext.cameraDetails
import com.subinkrishna.fpx.ext.displayDate
import com.subinkrishna.fpx.ext.hasLocation
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.ui.vh.*
import java.util.*

/** Photo details adapter */
class PhotoDetailsAdapter : RecyclerView.Adapter<BaseDetailsItemViewHolder>() {

    companion object {
        // ViewTypes
        private const val TYPE_TITLE_USER_DATE = 0
        private const val TYPE_PULSE = 1
        private const val TYPE_CAMERA = 2
        private const val TYPE_LOCATION = 3
        private const val TYPE_DATE = 4
    }

    private var photo: Photo? = null

    // This holds the list of view types that needs to be shown
    // in the details (based on the incoming item)
    private var views: List<Int> = emptyList()

    fun submit(photo: Photo?) {
        this.photo = photo
        views = prepareViews(photo)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseDetailsItemViewHolder {
        return when (viewType) {
            TYPE_TITLE_USER_DATE -> PhotoTitleViewHolder.create(parent)
            TYPE_PULSE -> PulseViewHolder.create(parent)
            TYPE_DATE -> DateDetailsViewHolder.create(parent)
            TYPE_CAMERA -> CameraDetailsViewHolder.create(parent)
            TYPE_LOCATION -> LocationDetailsViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int = views.size

    override fun onBindViewHolder(holder: BaseDetailsItemViewHolder, position: Int) {
        holder.bind(photo)
    }

    override fun getItemViewType(position: Int): Int = getItem(position)

    fun getItem(position: Int): Int = views[position]

    // Internal methods

    private fun prepareViews(photo: Photo?): List<Int> {
        val views = LinkedList<Int>()
        photo?.let {
            views += TYPE_TITLE_USER_DATE
            views += TYPE_PULSE
            if (!photo.displayDate.isNullOrBlank()) {
                views += TYPE_DATE
            }
            if (!photo.cameraDetails.isNullOrBlank()) {
                views += TYPE_CAMERA
            }
            if (photo.hasLocation) {
                views += TYPE_LOCATION
            }
        }
        return views
    }
}

