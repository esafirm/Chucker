package com.chucker.tracker.internal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chucker.tracker.internal.data.entity.TrackerLog

@Database(
    entities = [
        TrackerLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapTypeConverter::class)
internal abstract class TrackerLogDatabase : RoomDatabase() {
    abstract fun trackerLogDao(): TrackerLogDao

    companion object {
        private const val DB_NAME = "chucker_tracker_log.db"

        fun create(applicationContext: Context): TrackerLogDatabase {
            return Room.databaseBuilder(applicationContext, TrackerLogDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}