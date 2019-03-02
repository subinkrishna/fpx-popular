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
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.di.ServiceLocator
import com.subinkrishna.fpx.stream.model.NetworkState
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
        /** Called on clicking an item in the photo grid */
        fun onItemClick(v: View, position: Int)

        /** Called back for parent's preference on the start position */
        fun startPosition(): Int
    }

    var callback: Callback? = null

    private val gridSpacing by lazy { resources.getDimension(R.dimen.grid_spacing).toInt() }
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var photoGrid: RecyclerView

    // ViewModel which belongs to parent
    // activity so that state can be shared with other fragments
    // if needed
    private val viewModel by lazy {
        activity?.run {
            val factory = PhotoStreamViewModel.Factory(
                app = application,
                api = ServiceLocator.get().api()
            )
            ViewModelProviders.of(this, factory)[PhotoStreamViewModel::class.java]
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Grid adapter
    private val streamAdapter by lazy {
        PhotoStreamAdapter(
            cornerRadius = context?.resources?.getDimensionPixelSize(R.dimen.photo_corner_radius) ?: 0,
            itemClickListener = View.OnClickListener { v ->
                val lm = photoGrid.layoutManager as StaggeredGridLayoutManager
                val position = lm.getPosition(v)
                callback?.onItemClick(v, position)
            },
            retryButtonClickListener = View.OnClickListener { retry() }
        ).apply {
            setHasStableIds(true)
        }
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

        photoGrid.doOnLayout {
            val lm = photoGrid.layoutManager as StaggeredGridLayoutManager
            val position = callback?.startPosition() ?: -1
            if (position >= 0) {
                val v = lm.findViewByPosition(position)
                // Ask the grid to scroll to the position if the view for the position is null
                // or is only partially visible
                if (v == null || lm.isViewPartiallyVisible(v, false, true)) {
                    photoGrid.post { photoGrid.scrollToPosition(position) }
                }
            }
        }

        // Observe changes to network state and update the UI accordingly
        viewModel.networkState.observe(this, Observer {
            streamAdapter.setNetworkState(it)
            swipeRefreshLayout.isRefreshing = it is NetworkState.Loading && it.page == 1
        })

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
        Timber.d("==> $state")
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

        swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }

    private fun retry() {
        viewModel.retry()
        // This is to avoid recycler view to jump to the end of page once the initial
        // page is loaded after a retry. This may not be ideal, but works!
        // May need to figure out a better way to handle this.
        if (streamAdapter.getItemViewType(0) != PhotoStreamAdapter.TYPE_PHOTO) {
            photoGrid.doOnNextLayout {
                photoGrid.post { photoGrid.scrollToPosition(0) }
            }
        }
    }
}