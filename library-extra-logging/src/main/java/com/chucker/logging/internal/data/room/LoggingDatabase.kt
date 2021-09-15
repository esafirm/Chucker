package com.chucker.logging.internal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chucker.logging.internal.data.entity.LogData

@Database(
    entities = [
        LogData::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class LoggingDatabase : RoomDatabase() {
    abstract fun loggingDao(): LoggingDao

    companion object {
        private const val DB_NAME = "chucker_logger.db"

        fun create(applicationContext: Context): LoggingDatabase {
            return Room.databaseBuilder(applicationContext, LoggingDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}