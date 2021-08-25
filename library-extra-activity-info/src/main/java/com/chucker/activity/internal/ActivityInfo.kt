package com.chucker.activity.internal

import java.io.Serializable

internal data class ActivityInfo(
        val simpleName: String,
        val packageName: String,
        val callingActivity: String,
        val fragments: List<String>,
        val otherInfo: Map<String, String>
) : Serializable