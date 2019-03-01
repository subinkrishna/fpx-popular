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

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
import com.subinkrishna.fpx.service.model.Photo
import com.subinkrishna.fpx.stream.model.NetworkState
import com.subinkrishna.fpx.stream.ui.vh.ImageItemViewHolder
import com.subinkrishna.fpx.stream.ui.vh.NetworkStateViewHolder

/** Photo stream adapter */
class PhotoStreamAdapter(
    private val itemClickListener: View.OnClickListener,
    private val retryButtonClickListener: View.OnClickListener
) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(PhotoDiff()) {

    companion object {
        // View types
        const val TYPE_PHOTO = 0
        const val TYPE_NETWORK_STATE = 1
    }

    // Holds the latest network state
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NETWORK_STATE -> {
                NetworkStateViewHolder.create(parent, retryButtonClickListener)
            }
            else -> {
                ImageItemViewHolder.create(parent).also {
                    it.itemView.setOnClickListener(itemClickListener)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageItemViewHolder -> holder.bind(getItem(position))
            is NetworkStateViewHolder -> {
                val lp = holder.itemView.layoutParams as? LayoutParams
                lp?.isFullSpan = true
                holder.bind(networkState)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            (hasExtraRow() && position == itemCount - 1) -> TYPE_NETWORK_STATE
            else -> TYPE_PHOTO
        }
    }

    override fun getItemId(position: Int): Long {
        return when {
            (hasExtraRow() && position == itemCount - 1) -> 0
            else -> getItem(position)?.id ?: -1
        }
    }

    /**
     * Sets the latest network state. Network status will be
     * added to (or removed from) the bottom of the view based on the incoming status
     *
     * @param newState
     */
    fun setNetworkState(newState: NetworkState?) {
        val oldState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && oldState != newState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    /** Checks if an extra row is needed */
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.Ready
    }
}