package com.chucker.tracker.internal.data.repository

import androidx.lifecycle.LiveData
import com.chucker.tracker.internal.data.entity.TrackerLog

internal interface TrackerLogRepository {

    suspend fun insertLog(log: TrackerLog)

    suspend fun deleteAllTransactions()

    fun getFilteredLog(query: String): LiveData<List<String>>

    fun getAllLogLiveData(): LiveData<List<String>>

    suspend fun getAllLog(): List<String>
}