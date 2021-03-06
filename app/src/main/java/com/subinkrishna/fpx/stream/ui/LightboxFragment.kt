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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.di.ServiceLocator
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.model.PhotoStreamViewModel
import com.subinkrishna.fpx.stream.ui.view.LightboxPagerAdapter

/**
 * Paged lightbox fragment implementation that renders fullscreen photos
 * in a horizontal [ViewPager]
 */
class LightboxFragment : Fragment() {

    /** Callback interface definition */
    interface Callback {
        /** Called back when the ViewPager page changes */
        fun onPageChange(position: Int)

        /** Called back on "close" button click */
        fun onClose()

        /** Called back on "info" button click */
        fun onInfo(item: Photo?)
    }

    companion object {
        // Argument keys
        private const val KEY_POSITION = "LightboxFragment.KEY_POSITION"

        /** Creates a new instance of LightboxFragment */
        fun create(startPosition: Int = 0): LightboxFragment {
            val args  = Bundle()
            args.putInt(KEY_POSITION, startPosition)
            return LightboxFragment().apply { arguments = args}
        }
    }

    var callback: Callback? = null
    var currentItem: Int
        get() = pager.currentItem
        set(value) { pager.setCurrentItem(value, false) }

    // Note:
    // This implementation of lightbox pager uses androidx's ViewPager2 library
    // which is still in alpha. The RecyclerView based ViewPager2 is beneficial
    // since we can use the standard PagedListAdapter just like the photo
    // stream grid. Since PagedListAdapter accepts the same PagedList object
    // shared with photo grid, pagination works out of box even with the pager.
    // The only downside is that the library is still alpha and may not
    // be as stable as we may like.
    private lateinit var pager: ViewPager2
    private lateinit var closeButton: ImageButton
    private lateinit var infoButton: ImageButton
    private val pagerAdapter = LightboxPagerAdapter().apply {
        setHasStableIds(true)
    }

    // LightboxFragment shares the ViewModel which belongs to parent
    // activity so that state can be shared with other fragments
    private val viewModel by lazy {
        activity?.run {
            val factory = PhotoStreamViewModel.Factory(
                app = application,
                api = ServiceLocator.get().api()
            )
            ViewModelProviders.of(this, factory)[PhotoStreamViewModel::class.java]
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_paged_lightbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        closeButton = view.findViewById<ImageButton>(R.id.closeButton).apply {
            setOnClickListener {
                callback?.onClose()
            }
        }

        infoButton = view.findViewById<ImageButton>(R.id.infoButton).apply {
            setOnClickListener {
                callback?.onInfo(pagerAdapter.getItemAt(pager.currentItem))
            }
        }

        pager = view.findViewById<ViewPager2>(R.id.pager).apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    callback?.onPageChange(position)
                }
            })
        }

        // Set the start position in the pager
        if (null == savedInstanceState) {
            val startPosition = arguments?.getInt(KEY_POSITION) ?: 0
            pager.post { currentItem = startPosition }
        }

        // Observe view state & render the changes as the arrive
        viewModel.viewStateLive().observe(this, Observer {
            pagerAdapter.submitList(it.items)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as? Callback
    }
}