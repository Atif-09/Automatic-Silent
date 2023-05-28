package com.example.autosilentapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTime(startTime: TimeEntities)

    @Update
    suspend fun updateTime(startTime: TimeEntities)

    @Delete
    suspend fun deleteTime(startTime: TimeEntities)

    @Query("SELECT * FROM time_record")
    fun getAllTimes(): LiveData<List<TimeEntities>>
    
    @Query("SELECT * FROM time_record ORDER BY id DESC LIMIT 1")
    suspend fun getRecentTime(): TimeEntities
}