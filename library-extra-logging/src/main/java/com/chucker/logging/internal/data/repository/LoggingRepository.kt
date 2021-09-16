package com.chucker.logging.internal.data.repository

import com.chucker.logging.internal.data.entity.LogData

internal interface LoggingRepository {

    suspend fun insertLog(logData: LogData)

    suspend fun deleteAllLog()

    suspend fun getFilteredLog(tag: String, query: String): List<LogData>

    suspend fun getAllLogLiveData(): List<LogData>

    suspend fun getAllTag(): List<String>
}