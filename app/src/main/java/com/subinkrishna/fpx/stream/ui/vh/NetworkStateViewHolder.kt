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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.subinkrishna.fpx.R
import com.subinkrishna.fpx.stream.model.NetworkState

/** Network state view holder */
class NetworkStateViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    companion object {
        fun create(
            parent: ViewGroup,
            retryButtonClickListener: View.OnClickListener
        ): NetworkStateViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_network_state, parent, false)
            return NetworkStateViewHolder(v).also {
                it.networkStatusText.setOnClickListener(retryButtonClickListener)
            }
        }
    }

    private val networkProgressBar = v.findViewById<ProgressBar>(R.id.networkStateProgress)
    private val networkStatusText = v.findViewById<TextView>(R.id.networkStateErrorText)

    fun bind(state: NetworkState?) {
        networkProgressBar.isVisible = state == NetworkState.Loading
        networkStatusText.isVisible = state == NetworkState.Error
    }
}