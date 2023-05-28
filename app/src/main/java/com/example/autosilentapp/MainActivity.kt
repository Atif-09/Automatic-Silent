package com.example.autosilentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var timeDB: TimeDB
    private lateinit var timeDao: PrayerTimesDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeDB = TimeDB.getDatabase(applicationContext)
        timeDao = timeDB.prayerTimesDao()
        GlobalScope.launch {
            timeDao.insertPrayerTimes(PrayerTimesEntity(0,"12:12","12:15",
                "12:18","12:21" ,"12:24"))
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.homeFragment)



        // Insert prayer times into the database


    }
}