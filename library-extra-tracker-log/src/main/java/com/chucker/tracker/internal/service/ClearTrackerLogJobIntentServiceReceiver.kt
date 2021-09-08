package com.chucker.tracker.internal.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class ClearTrackerLogJobIntentServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ClearTrackerLogDatabaseService.enqueueWork(context, intent)
    }
}
