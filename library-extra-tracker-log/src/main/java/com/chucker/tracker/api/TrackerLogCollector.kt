package com.chucker.tracker.api

interface TrackerLogCollector {
    fun sendLog(map: Map<String, Any?>)
}