package com.chucker.tracker.internal.support

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal fun Context.showDialog(
    title: String,
    message: String,
    positiveButtonText: String?,
    negativeButtonText: String?,
    onPositiveClick: (() -> Unit)?,
    onNegativeClick: (() -> Unit)?
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClick?.invoke()
        }
        .setNegativeButton(negativeButtonText) { _, _ ->
            onNegativeClick?.invoke()
        }
        .show()
}