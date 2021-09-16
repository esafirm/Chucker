package com.chucker.logging.internal.support

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.text.SimpleDateFormat

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

internal fun String.formatLog(): String {
    return try {
        JSONObject(this).toString(2)
    } catch (ignored: Exception) {
        this
    }
}

internal fun Long.formatDate(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(this)
}