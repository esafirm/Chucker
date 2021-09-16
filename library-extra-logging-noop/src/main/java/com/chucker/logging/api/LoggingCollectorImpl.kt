package com.chucker.logging.api

class LoggingCollectorImpl() : LoggingCollector {

    override fun sendLog(tag: String, message: String) {
        // noop
    }

    override fun sendLog(tag: String, message: Any) {
        // noop
    }
}