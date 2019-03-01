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
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.service.impl.NetworkPhotoApi
import com.subinkrishna.fpx.stream.model.PhotoStreamViewModel
import com.subinkrishna.fpx.stream.model.ViewState
import com.subinkrishna.fpx.stream.ui.view.PhotoStreamAdapter
import timber.log.Timber

/**
 * Photo stream fragment. This implementation take in [ViewState]
 * and updates its UI state accordingly
 */
class PhotoStreamFragment : Fragment() {

    /** Callback interface definition */
    interface Callback {
        /**
         * Called on clicking an item in the photo grid
         *
         * @param v - clicked view
         * @param position - adapter position of the selected item
         */
        fun onItemClick(v: View, position: Int)
    }

    var callback: Callback? = null

    private val gridSpacing by lazy { resources.getDimension(R.dimen.grid_spacing).toInt() }
    private lateinit var photoGrid: RecyclerView
    private lateinit var progressIndicator: ProgressBar

    // ViewModel which belongs to parent
    // activity so that state can be shared with other fragments
    // if needed
    private val viewModel by lazy {
        activity?.run {
            val factory = PhotoStreamViewModel.Factory(
                app = application,
                api = NetworkPhotoApi()
            )
            ViewModelProviders.of(this, factory)[PhotoStreamViewModel::class.java]
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Grid adapter
    private val streamAdapter = PhotoStreamAdapter(
        onItemClick = View.OnClickListener { v ->
            val lm = photoGrid.layoutManager as StaggeredGridLayoutManager
            val position = lm.getPosition(v)
            callback?.onItemClick(v, position)
        }
    ).apply {
        setHasStableIds(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_stream, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureUi(view)

        // Forcing the recycler view to scroll to 0th position
        // to avoid SGLM's gap filling feature that causes
        // views to move around/slide. May need to find a better way.
        photoGrid.post {
            // todo: pick 0 or pagers last position
            photoGrid.scrollToPosition(10)
        }

        // Observe view state & render the changes as the arrive
        viewModel.viewStateLive().observe(this, Observer {
            render(it)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as? Callback
    }


    // Internal methods

    private fun render(state: ViewState) {
        Timber.d("==> ${state.isLoading}")
        progressIndicator.isVisible = state.isLoading
        streamAdapter.submitList(state.items)
    }

    private fun configureUi(root: View) {
        // Title
        root.findViewById<TextView>(R.id.titleText).apply {
            setText(R.string.app_name)
            setOnClickListener {
                photoGrid.smoothScrollToPosition(0)
            }
        }

        // todo: may need to setup Glide's PreloadModelProvider to prefetch images
        photoGrid = root.findViewById<RecyclerView>(R.id.photoGrid).apply {
            setHasFixedSize(true)
            adapter = streamAdapter
            // Disabling the move animation to hide
            // SGLM's span adjustment animation
            (itemAnimator as SimpleItemAnimator).apply {
                moveDuration = 0
                changeDuration = 0
            }
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

        progressIndicator = root.findViewById(R.id.progressIndicator)
    }
}