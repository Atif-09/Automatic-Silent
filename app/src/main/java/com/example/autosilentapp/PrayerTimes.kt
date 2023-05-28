package com.example.autosilentapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrayerTimesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTimes: PrayerTimesEntity)

    @Query("SELECT * FROM prayer_times WHERE id = :id")
    suspend fun getPrayerTimesById(id: Int): PrayerTimesEntity
    @Query("SELECT * FROM prayer_times WHERE id = :id")
    suspend fun getAllPrayerTimes(id: Int): List<PrayerTimesEntity>
}
