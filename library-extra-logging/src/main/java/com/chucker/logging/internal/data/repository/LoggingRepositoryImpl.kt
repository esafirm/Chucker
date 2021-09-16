package com.chucker.logging.internal.data.repository

import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.data.room.LoggingDatabase

internal class LoggingRepositoryImpl(
    private val database: LoggingDatabase
) : LoggingRepository {
    override suspend fun insertLog(logData: LogData) {
        database.loggingDao().insert(logData)
    }

    override suspend fun deleteAllLog() {
        database.loggingDao().deleteAll()
    }

    override suspend fun getFilteredLog(tag: String, query: String): List<LogData> {
        return if (tag.isBlank()) {
            database.loggingDao().getFiltered("%$query%")
        } else {
            database.loggingDao().getFilteredWithTag(tag, "%$query%")
        }
    }

    override suspend fun getAllLogLiveData(): List<LogData> {
        return database.loggingDao().getAllLiveData()
    }

    override suspend fun getAllTag(): List<String> {
        return database.loggingDao().getTags()
    }
}