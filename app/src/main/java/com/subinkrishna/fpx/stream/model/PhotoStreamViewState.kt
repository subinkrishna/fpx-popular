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
package com.subinkrishna.fpx.stream.model

import com.subinkrishna.fpx.service.model.Photo

/** Photo stream view state */
data class ViewState(
    val isLoading: Boolean = false,
    val items: List<Photo>? = null,
    val error: Error? = null
)

/** Error */
sealed class Error {
    object Network : Error()
    object Unknown : Error()
}