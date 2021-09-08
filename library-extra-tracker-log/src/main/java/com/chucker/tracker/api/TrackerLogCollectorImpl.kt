package com.chucker.tracker.api

import com.chucker.tracker.internal.data.entity.TrackerLog
import com.chucker.tracker.internal.data.repository.TrackerLogRepositoryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TrackerLogCollectorImpl(
    private val scope: CoroutineScope = MainScope()
) : TrackerLogCollector {
    override fun sendLog(map: Map<String, Any?>) {
        val trackerLog = TrackerLog(logString = map)
        scope.launch {
            TrackerLogRepositoryProvider.get().insertLog(trackerLog)
        }
    }
}