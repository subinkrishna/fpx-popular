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

package com.subinkrishna.fpx.ktx

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.Px

/**
 * Standard [ViewOutlineProvider] implementations
 */
class ViewOutlineProviders {

    companion object {
        // Default singleton instances
        private val CIRCULAR by lazy { Circular() }

        @JvmStatic
        fun roundRect(
            @Px cornerRadius: Float,
            respectPadding: Boolean = false
        ): ViewOutlineProvider {
            return RoundRect(cornerRadius, respectPadding)
        }

        @JvmStatic
        fun circular(): ViewOutlineProvider = CIRCULAR
    }

    /** Round rect outline provider implementation */
    class RoundRect(
            @Px private val cornerRadius: Float,
            private val respectPadding: Boolean = false
    ) : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            view?.let {
                val left = if (respectPadding) it.paddingLeft else 0
                val top = if (respectPadding) it.paddingTop else 0
                val right = if (respectPadding) it.width - it.paddingRight else it.width
                val bottom = if (respectPadding) it.height - it.paddingBottom else it.height
                outline?.setRoundRect(left, top, right, bottom, cornerRadius)
            }
        }
    }

    /** Circular view outline provider implementation */
    class Circular : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            view?.let {
                val left = it.paddingLeft
                val top = it.paddingTop
                val right = it.width - it.paddingRight
                val bottom = it.height - it.paddingBottom
                outline?.setOval(left, top, right, bottom)
            }
        }
    }
}