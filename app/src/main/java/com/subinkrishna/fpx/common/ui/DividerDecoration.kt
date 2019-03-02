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
package com.subinkrishna.fpx.common.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * Divider decoration
 */
open class DividerDecoration : RecyclerView.ItemDecoration() {

    private val dividerPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = 0xffcccccc.toInt()
            strokeWidth = 1f // 1 px
        }
    }

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.children.forEach { child ->
            if (shouldDrawDividerAbove(parent, child)) {
                val top = child.top + child.translationY
                c.drawLine(0f, top, parent.width.toFloat(), top, dividerPaint)
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    open fun shouldDrawDividerAbove(
        parent: RecyclerView,
        child: View
    ): Boolean {
        return parent.getChildAdapterPosition(child) > 0
    }
}