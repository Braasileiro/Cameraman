package com.brasileiro.cameraman.extension

import android.view.View
import android.widget.TextView

/**
 * @author Lucas Cota
 * @since 14/06/2019 11:55
 */

internal fun View.visible(visible: Boolean) {
    when (visible) {
        true -> this.visibility = View.VISIBLE
        false -> this.visibility = View.GONE
    }
}

internal fun TextView.toText(): String? {
    return this.text.toString()
}
