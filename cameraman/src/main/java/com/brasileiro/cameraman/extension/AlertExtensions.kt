package com.brasileiro.cameraman.extension

import android.view.View
import android.widget.Toast
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

/**
 * @author Lucas Cota
 * @since 14/06/2019 12:14
 */

internal fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

internal fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

internal fun createDialog(
    context: Context,
    view: Int,
    title: String? = null,
    positiveText: String? = null,
    negativeText: String? = null,
    positiveAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null,
    cancelable: Boolean = false
): AlertDialog.Builder {

    val alertDialog = AlertDialog.Builder(context)

    alertDialog.setView(view)
    alertDialog.setCancelable(cancelable)

    when {
        title != null -> alertDialog.setTitle(title)

        positiveAction != null -> alertDialog.setPositiveButton(positiveText) { _, _ -> positiveAction() }
    }

    when {
        negativeAction != null -> alertDialog.setNegativeButton(negativeText) { _, _ -> negativeAction() }

        else -> alertDialog.setNegativeButton(negativeText) { dialog, _ -> dialog.dismiss() }
    }

    return alertDialog
}
