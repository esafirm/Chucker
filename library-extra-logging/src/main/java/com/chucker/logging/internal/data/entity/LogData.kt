package com.chucker.logging.internal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logging")
internal data class LogData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    var tag: String = "",

    @ColumnInfo(name = "log")
    var logString: String = "",

    var timeStamp: Long = 0L
)