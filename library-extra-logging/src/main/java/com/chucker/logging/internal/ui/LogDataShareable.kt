package com.chucker.logging.internal.ui

import android.content.Context
import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.support.Sharable
import com.chuckerteam.chucker.R
import okio.Buffer
import okio.Source
import org.json.JSONObject
import java.text.SimpleDateFormat

internal class LogDataShareable(
    private val logs: List<LogViewParam>,
) : Sharable {
    override fun toSharableContent(context: Context): Source = Buffer().apply {
        writeUtf8(
            logs.joinToString(
                separator = "\n${context.getString(R.string.chucker_export_separator)}\n",
                prefix = "${context.getString(R.string.chucker_export_prefix)}\n",
                postfix = "\n${context.getString(R.string.chucker_export_postfix)}\n"
            ) {
                it.tag + "\n" + it.logText + "\n" + it.dateText
            }
        )
    }
}