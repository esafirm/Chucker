package com.chucker.tracker.internal.ui

import android.content.Context
import com.chucker.tracker.internal.support.Sharable
import com.chuckerteam.chucker.R
import okio.Buffer
import okio.Source

internal class TrackerLogsShareable(
    private val logs: List<String>,
) : Sharable {
    override fun toSharableContent(context: Context): Source = Buffer().apply {
        writeUtf8(
            logs.joinToString(
                separator = "\n${context.getString(R.string.chucker_export_separator)}\n",
                prefix = "${context.getString(R.string.chucker_export_prefix)}\n",
                postfix = "\n${context.getString(R.string.chucker_export_postfix)}\n"
            )
        )
    }
}