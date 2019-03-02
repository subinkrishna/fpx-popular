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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.ui.view.PhotoDetailsAdapter

/** Photo details. Shows the detailed information about the photo */
class PhotoDetailsFragment : BottomSheetDialogFragment() {

    companion object {
        // Argument keys
        private const val KEY_PHOTO = "PhotoDetailsFragment.KEY_PHOTO"

        /** Creates a new instance of PhotoDetailsFragment */
        fun create(photo: Photo): PhotoDetailsFragment {
            val args  = Bundle()
            args.putParcelable(KEY_PHOTO, photo)
            return PhotoDetailsFragment().apply { arguments = args}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val detailsAdapter = PhotoDetailsAdapter()
        view.findViewById<RecyclerView>(R.id.list).apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(this@PhotoDetailsFragment.context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(
                this@PhotoDetailsFragment.context,
                DividerItemDecoration.VERTICAL))
        }

        val photo = arguments?.getParcelable(KEY_PHOTO) as? Photo
        detailsAdapter.submit(photo)
    }
}