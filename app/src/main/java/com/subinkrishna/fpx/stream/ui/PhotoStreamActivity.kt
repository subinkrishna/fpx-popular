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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.subinkrishna.fpx.R
import timber.log.Timber

/**
 * Photo stream activity implementation. This activity renders the
 * paginated photo stream as a grid.
 */
class PhotoStreamActivity : AppCompatActivity(),
    PhotoStreamFragment.Callback,
    LightboxFragment.Callback {

    // Holds the last known lightbox position
    // This is used by the photo grid to scroll to the given position
    var lastKnownLightboxPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_stream)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, PhotoStreamFragment(), "grid")
                .commit()
        } else {
            val f = supportFragmentManager.findFragmentByTag("grid")
            Timber.d("--> $f")
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is LightboxFragment) {
            lastKnownLightboxPosition = currentFragment.currentItem
        }
        super.onBackPressed()
    }

    // PhotoStreamFragment.Callback

    override fun onItemClick(v: View, position: Int) {
        // Launch the lightbox on selecting an item
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .setCustomAnimations(
                R.anim.anim_lightbox_enter,
                0, 0,
                R.anim.anim_lightbox_exit)
            .replace(R.id.container, LightboxFragment.create(position))
            .addToBackStack(null)
            .commit()
    }

    override fun startPosition(): Int = lastKnownLightboxPosition


    // LightboxFragment.Callback

    override fun onPageChange(position: Int) {
        lastKnownLightboxPosition = position
    }

    override fun onClose() {
        // Get the current lightbox position
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment is LightboxFragment) {
            lastKnownLightboxPosition = currentFragment.currentItem
        }
        // And pop the back stack to go back to grid
        supportFragmentManager.popBackStack()
    }
}
