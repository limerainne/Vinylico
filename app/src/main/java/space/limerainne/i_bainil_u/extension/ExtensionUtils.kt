/*
 * Copyright 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.limerainne.i_bainil_u.extension

import java.text.DateFormat
import java.util.*

fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

fun String.toLongOrElse(default: Long): Long  {
    try {
        return this.toLong()
    }   catch (e: NumberFormatException)    {
        return default
    }
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
fun Int.format(digits: Int, spacer: Char = '0') = java.lang.String.format("%${spacer}${digits}d", this)