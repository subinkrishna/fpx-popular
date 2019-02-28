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

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.service.impl.NetworkPhotoApi
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.model.PhotoStreamViewModel
import com.subinkrishna.fpx.stream.model.ViewState
import timber.log.Timber

/**
 * Photo stream activity implementation. This activity renders the
 * paginated photo stream as a grid.
 */
class PhotoStreamActivity : AppCompatActivity() {

    private val gridSpacing by lazy { resources.getDimension(R.dimen.grid_spacing).toInt() }

    private lateinit var photoGrid: RecyclerView
    private lateinit var progressIndicator: ProgressBar
    private lateinit var pagedLightbox: PagedImageLightboxView

    private val streamAdapter = PhotoStreamAdapter(
        onItemClick = View.OnClickListener { handleItemClick(it) }
    ).apply {
        setHasStableIds(true)
    }

    private val viewModel by lazy {
        val factory = PhotoStreamViewModel.Factory(
            app = application,
            api = NetworkPhotoApi())
        ViewModelProviders.of(this, factory)[PhotoStreamViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_stream)
        configureUi()

        // Forcing the recycler view to scroll to 0th position
        // to avoid SGLM's gap filling feature that causes
        // views to move around/slide. May need to find a better way.
        photoGrid.post { photoGrid.scrollToPosition(0) }

        // Observe view state & render the changes as the arrive
        viewModel.viewStateLive().observe(this, Observer { render(it) })
    }


    // Internal methods

    private fun render(state: ViewState) {
        progressIndicator.isVisible = state.isLoading
        streamAdapter.submitList(state.items)
        pagedLightbox.bind(state.items)
    }

    private fun configureUi() {
        // todo:
        // May need to setup Glide's PreloadModelProvider to prefetch
        // images
        photoGrid = findViewById<RecyclerView>(R.id.photoGrid).apply {
            setHasFixedSize(true)
            adapter = streamAdapter
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect, view: View,
                    parent: RecyclerView, state: RecyclerView.State
                ) {
                    outRect.set(gridSpacing, gridSpacing, gridSpacing, gridSpacing)
                }
            })
        }

        progressIndicator = findViewById(R.id.progressIndicator)
        pagedLightbox = findViewById(R.id.pagedLightbox)
    }

    private fun handleItemClick(v: View) {
        val lm = photoGrid.layoutManager as StaggeredGridLayoutManager
        val position = lm.getPosition(v)
        pagedLightbox.isVisible = true
        pagedLightbox.currentItem = position
    }
}
