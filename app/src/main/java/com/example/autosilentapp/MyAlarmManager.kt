package com.example.autosilentapp
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MyAlarmManager(private val context: Context) {


    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarms(startTime: String, endTime: String) { // Retrieve the TimeData from the Room database
        if (startTime != null) {
            scheduleSilentAlarm(startTime) // Schedule the silent alarm
            scheduleRingModeAlarm(endTime) // Schedule the ring mode alarm
            Log.d("checkTime" , "start time  ----${startTime}")
            Log.d("checkTime" , "end time -----${endTime}")

        }
    }
    private fun scheduleSilentAlarm(startTime: String) {
        val startTimeMillis = getTimeInMillis(startTime)
        val requestCode = startTime.hashCode()
        val silentIntent = Intent(context, SilentModeReceiver::class.java).apply {
            action = "com.example.autosilentapp.ACTION_SILENT_MODE"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            silentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("checkTime" , "start time Milli -------- $startTimeMillis")

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            startTimeMillis,
            pendingIntent
        )
    }

    private fun scheduleRingModeAlarm(endTime: String) {
        val endTimeMillis = getTimeInMillis(endTime)
        val requestCode = endTime.hashCode()
        val ringModeIntent = Intent(context, SilentModeReceiver::class.java).apply {
            action = "com.example.autosilentapp.ACTION_RING_MODE"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            ringModeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("checkTime" , "end time Milli ----- $endTimeMillis")

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            endTimeMillis,
            pendingIntent
        )
    }
    
    private fun getTimeInMillis(timeString: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val time = sdf.parse("$currentDate $timeString")
        val calendar = Calendar.getInstance()
        calendar.time = time
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis
    }

    fun cancelAlarms(startTime: PendingIntent, endTime: AlarmManager) {
        val silentRequestCode = startTime.hashCode()
        val ringRequestCode = endTime.hashCode()
        val silentIntent = Intent(context, SilentModeReceiver::class.java).apply {
            action = "com.example.autosilentapp.ACTION_SILENT_MODE"
        }
        val ringModeIntent = Intent(context, SilentModeReceiver::class.java).apply {
            action = "com.example.autosilentapp.ACTION_RING_MODE"
        }
        val silentPendingIntent = PendingIntent.getBroadcast(
            context,
            silentRequestCode,
            silentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val ringModePendingIntent = PendingIntent.getBroadcast(
            context,
            ringRequestCode,
            ringModeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(silentPendingIntent)
        alarmManager.cancel(ringModePendingIntent)
    }

}
