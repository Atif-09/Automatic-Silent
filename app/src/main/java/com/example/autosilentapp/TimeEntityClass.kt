package com.example.autosilentapp

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "time_record",
    indices = [Index(value = ["startTime", "endTime"], unique = true)]
)
data class TimeEntities(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "startTime") val startTime: String,
    @ColumnInfo(name = "endTime") val endTime: String,

) : Parcelable

@Entity(tableName = "prayer_times")
data class PrayerTimesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "fajr") val fajr: String,
    @ColumnInfo(name = "zuhr") val zuhr: String,
    @ColumnInfo(name = "asr") val asr: String,
    @ColumnInfo(name = "maghrib") val maghrib: String,
    @ColumnInfo(name = "isha") val isha: String
)

