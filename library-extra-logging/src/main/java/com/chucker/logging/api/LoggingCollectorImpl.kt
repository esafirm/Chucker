package com.chucker.logging.api

import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.chucker.logging.internal.support.BundleTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoggingCollectorImpl(
    private val scope: CoroutineScope = MainScope(),
) : LoggingCollector {

    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapterFactory(BundleTypeAdapterFactory())
            .create()
    }

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
            try {
                val stringifyMessage = gson.toJson(message)
                val logData = LogData(tag = tag, logString = stringifyMessage, timeStamp = System.currentTimeMillis())
                LoggingRepositoryProvider.get().insertLog(logData)
            } catch (e: Throwable) {
                sendLog(tag, message.toString())
                sendLog("chucker-error", e.localizedMessage.orEmpty())
            }
        }
    }
}