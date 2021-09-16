package com.chucker.logging.internal.data.repository

import android.content.Context
import com.chucker.logging.internal.data.room.LoggingDatabase

internal object LoggingRepositoryProvider {

    private var loggingRepositoryBuilder: (() -> LoggingRepository)? = null
    private var loggingRepository: LoggingRepository? = null

    fun get(): LoggingRepository {
        if (loggingRepository == null) {
            loggingRepository = loggingRepositoryBuilder?.invoke()
        }
        return checkNotNull(loggingRepository) {
            "You can't access the logging repository if you don't initialize it!"
        }
    }

    fun initialize(context: Context) {
        if (loggingRepositoryBuilder == null) {
            loggingRepositoryBuilder = {
                LoggingRepositoryImpl(LoggingDatabase.create(context))
            }
        }
    }
}