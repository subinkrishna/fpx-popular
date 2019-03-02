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
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.ext.cameraDetails
import com.subinkrishna.fpx.ext.displayDate
import com.subinkrishna.fpx.ext.locationDetails
import com.subinkrishna.fpx.service.model.Photo
import java.text.DecimalFormat

/** Base ViewHolder for photo details */
abstract class BaseDetailsItemViewHolder(v: View): RecyclerView.ViewHolder(v) {
    abstract fun bind(item: Photo?)
}

/** Photo title, username, date */
class PhotoTitleViewHolder(v: View) : BaseDetailsItemViewHolder(v) {
    companion object {
        fun create(parent: ViewGroup): PhotoTitleViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_photo_details_title_user_name, parent, false)
            return PhotoTitleViewHolder(v)
        }
    }

    private val titleText: TextView = v.findViewById(R.id.photoTitle)
    private val usernameText: TextView = v.findViewById(R.id.username)

    override fun bind(item: Photo?) {
        titleText.text = item?.name
        titleText.isVisible = !item?.name.isNullOrBlank()
        usernameText.text = item?.user?.username
        usernameText.isVisible = !item?.user?.username.isNullOrBlank()
    }
}

/** Pulse */
class PulseViewHolder(v: View) : BaseDetailsItemViewHolder(v) {
    companion object {
        fun create(parent: ViewGroup): PulseViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_photo_details_pulse, parent, false)
            return PulseViewHolder(v)
        }
    }

    private val viewCountText: TextView = v.findViewById(R.id.viewCount)
    private val commentCountText: TextView = v.findViewById(R.id.commentCount)
    private val voteCountText: TextView = v.findViewById(R.id.voteCount)

    override fun bind(item: Photo?) {
        viewCountText.text = format(item?.viewCount ?: 0)
        commentCountText.text = format(item?.commentCount ?: 0)
        voteCountText.text = format(item?.voteCount ?: 0)
    }

    private fun format(count: Int): String {
        val formatter = DecimalFormat("#,###,###")
        return formatter.format(count)
    }
}

/** Camera */
class CameraDetailsViewHolder(v: View) : BaseDetailsItemViewHolder(v) {
    companion object {
        fun create(parent: ViewGroup): CameraDetailsViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_photo_details_camera, parent, false)
            return CameraDetailsViewHolder(v)
        }
    }

    private val viewCountText: TextView = v.findViewById(R.id.cameraSettings)

    override fun bind(item: Photo?) {
        viewCountText.text = item?.cameraDetails
    }
}

/** Date & time */
class DateDetailsViewHolder(v: View) : BaseDetailsItemViewHolder(v) {
    companion object {
        fun create(parent: ViewGroup): DateDetailsViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_photo_details_date, parent, false)
            return DateDetailsViewHolder(v)
        }
    }

    private val dateText: TextView = v.findViewById(R.id.date)

    override fun bind(item: Photo?) {
        dateText.text = item?.displayDate
    }
}

/** Location */
class LocationDetailsViewHolder(v: View) : BaseDetailsItemViewHolder(v) {
    companion object {
        fun create(parent: ViewGroup): LocationDetailsViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_photo_details_location, parent, false)
            return LocationDetailsViewHolder(v)
        }
    }

    private val viewCountText: TextView = v.findViewById(R.id.location)

    override fun bind(item: Photo?) {
        viewCountText.text = item?.locationDetails
    }
}