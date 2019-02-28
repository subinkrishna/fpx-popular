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
import android.widget.ImageButton
import androidx.paging.PagedList
import androidx.viewpager2.widget.ViewPager2
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.service.model.Photo

/***
 * A view pager based lightbox that cycles through
 * the images in the stream
 */
class PagedImageLightboxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var currentItem: Int
        get() = pager.currentItem
        set(value) { pager.setCurrentItem(value, false) }

    private val pager: ViewPager2
    private val closeButton: ImageButton
    private val pagerAdapter = PagerAdapter().apply {
        setHasStableIds(true)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_paged_lightbox, this, true)
        pager = findViewById<ViewPager2>(R.id.pager).apply {
            adapter = pagerAdapter
        }
        closeButton = findViewById(R.id.closeButton)
    }

    fun bind(items: PagedList<Photo>?) {
        pagerAdapter.submitList(items)
    }

}

