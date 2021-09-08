package com.chucker.tracker.internal.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chucker.tracker.internal.data.entity.TrackerLog

@Dao
internal interface TrackerLogDao {
    @Query("SELECT id, log FROM tracker_log WHERE log LIKE :query ORDER BY id DESC")
    fun getFiltered(query: String): LiveData<List<TrackerLog>>

    @Query("SELECT * FROM tracker_log")
    fun getAllLiveData(): LiveData<List<TrackerLog>>

    @Query("SELECT * FROM tracker_log")
    suspend fun getAll(): List<TrackerLog>

    @Insert
    suspend fun insert(value: TrackerLog): Long?

    @Query("DELETE FROM tracker_log")
    suspend fun deleteAll()
}