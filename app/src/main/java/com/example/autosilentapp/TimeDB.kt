package com.example.autosilentapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TimeEntities::class,PrayerTimesEntity::class], version = 1, exportSchema = false)
abstract class TimeDB: RoomDatabase() {

    abstract fun TimeDao(): TimeDao
    abstract fun prayerTimesDao(): PrayerTimesDao










    companion object {
        @Volatile
        private var INSTANCE: TimeDB? = null

        fun getDatabase(context: Context): TimeDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeDB::class.java,
                    "time_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
