package com.chucker.logging.api

interface LoggingCollector {
    fun sendLog(tag: String, message: String)
    fun sendLog(tag: String, message: Any)
}