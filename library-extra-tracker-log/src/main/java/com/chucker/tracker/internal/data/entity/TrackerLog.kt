package com.chucker.tracker.internal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chucker.tracker.internal.data.room.MapTypeConverter

@Entity(tableName = "tracker_log")
internal data class TrackerLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "log")
    @TypeConverters(MapTypeConverter::class)
    var logString: Map<String, Any?>? = null
)