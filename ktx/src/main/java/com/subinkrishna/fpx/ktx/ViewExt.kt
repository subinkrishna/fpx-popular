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
@file:Suppress("NOTHING_TO_INLINE")
package com.subinkrishna.fpx.ktx

import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.Px

/**
 * Clips a view to circle
 *
 * @see ViewOutlineProviders.CIRCULAR
 */
inline fun View.clipToCircle() {
    clipTo(ViewOutlineProviders.circular())
}

/**
 * Clips a view to round rect
 *
 * @param cornerRadius
 * @param respectPadding
 * @see ViewOutlineProviders
 */
inline fun View.clipToRoundRect(
    @Px cornerRadius: Float,
    respectPadding: Boolean = false
) {
    clipTo(ViewOutlineProviders.roundRect(cornerRadius, respectPadding))
}

/**
 *  Clips the view using the provided outlineProvider
 *
 *  @param outlineProvider
 */
inline fun View.clipTo(outlineProvider: ViewOutlineProvider) {
    this.outlineProvider = outlineProvider
    clipToOutline = true
}