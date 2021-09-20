package com.chucker.logging.api

import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoggingCollectorImpl(
    private val scope: CoroutineScope = MainScope(),
    private val gson: Gson = Gson()
) : LoggingCollector {

    override fun sendLog(tag: String, message: String) {
        scope.launch {
            val log = LogData(tag = tag, logString = message, timeStamp = System.currentTimeMillis())
            LoggingRepositoryProvider.get().insertLog(log)
        }
    }

    override fun sendLog(tag: String, message: Any) {
        if (message is String) {
            sendLog(tag, message)
            return
        }
        scope.launch {
            val stringifyMessage = gson.toJson(message)
            val logData = LogData(tag = tag, logString = stringifyMessage, timeStamp = System.currentTimeMillis())
            LoggingRepositoryProvider.get().insertLog(logData)
        }
    }
}