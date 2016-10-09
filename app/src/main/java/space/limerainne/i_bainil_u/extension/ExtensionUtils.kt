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

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
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
fun Int.thirdCommaFormat() = java.lang.String.format("%,d", this)

fun Int.toDurationText(): String  {
    var result = ""

    if (this < 0)
        return "--:--"

    val exactSecond = this % 60
    val minute = this / 60
    val exactMinute = minute % 60
    val hour = minute / 60
    // NOTE ignore if hrs more than 23

    if (hour > 0)
        result = "${hour.format(2)}:"
    result += "${exactMinute.format(2)}:${exactSecond.format(2)}"

    return result
}

fun Long.toSizeText(): String  {
    if (this == 0L)
        return "-"

    // input's unit is in byte
    val unit: Double = 1024.0

    // into KiB
    var sizeInto = this / unit
    if (sizeInto <= unit)    {
        return "%.2f KiB".format(sizeInto)
    }

    // into MiB
    sizeInto /= unit
    if (sizeInto <= unit)    {
        return "%.2f MiB".format(sizeInto)
    }

    // into GiB
    sizeInto /= unit
    return "%.2f GiB".format(sizeInto)
}

fun String.toBitrateText(): String = if (this.length > 0) (this + "k") else "-"


fun View.setVisibility4(isVisible: Boolean): Int {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
    return if (isVisible == true) 1 else 0
}

fun fromHtml4(source: String): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
    else
        return Html.fromHtml(source)
}