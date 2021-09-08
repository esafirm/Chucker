package com.chucker.tracker.internal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.chucker.tracker.internal.data.entity.TrackerLog
import com.chucker.tracker.internal.data.room.TrackerLogDatabase
import com.chucker.tracker.internal.support.NotificationHelper

internal class TrackerLogRepositoryImpl(
    private val database: TrackerLogDatabase,
    private val notificationHelper: NotificationHelper?
) : TrackerLogRepository {
    override suspend fun insertLog(log: TrackerLog) {
        database.trackerLogDao().insert(log)
        notificationHelper?.show(database.trackerLogDao().getAll().size)
    }

    override suspend fun deleteAllTransactions() {
        database.trackerLogDao().deleteAll()
    }

    override fun getFilteredLog(query: String): LiveData<List<String>> {
        return database.trackerLogDao().getFiltered("%$query%").map(::mapTrackerLog)
    }

    override fun getAllLogLiveData(): LiveData<List<String>> {
        return database.trackerLogDao().getAllLiveData().map(::mapTrackerLog)
    }

    override suspend fun getAllLog(): List<String> {
        return database.trackerLogDao().getAll().map {
            it.logString?.let { map ->
                val stringBuilder = StringBuilder()
                map.keys.forEach { mapKey ->
                    stringBuilder.appendLine("$mapKey = ${map[mapKey]}")
                }
                stringBuilder.toString()
            } ?: ""
        }
    }

    private fun mapTrackerLog(trackerLogs: List<TrackerLog>): List<String> {
        return trackerLogs.map {
            it.logString?.let { map ->
                val stringBuilder = StringBuilder()
                map.keys.forEach { mapKey ->
                    stringBuilder.appendLine("$mapKey = ${map[mapKey]}")
                }
                stringBuilder.toString()
            } ?: ""
        }
    }
}