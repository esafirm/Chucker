package com.chucker.tracker.internal.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.chucker.tracker.internal.data.repository.TrackerLogRepositoryProvider
import com.chucker.tracker.internal.support.NotificationHelper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class ClearTrackerLogDatabaseService : JobIntentService() {
    private val scope = MainScope()

    override fun onHandleWork(intent: Intent) {
        TrackerLogRepositoryProvider.initialize(applicationContext)
        scope.launch {
            TrackerLogRepositoryProvider.get().deleteAllTransactions()
            NotificationHelper(applicationContext).dismissNotifications()
        }
    }

    companion object {
        private const val CLEAN_DATABASE_JOB_ID = 123321

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, ClearTrackerLogDatabaseService::class.java, CLEAN_DATABASE_JOB_ID, work)
        }
    }
}
