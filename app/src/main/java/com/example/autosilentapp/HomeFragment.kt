package com.example.autosilentapp

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autosilentapp.databinding.FragmentHomeBinding
import com.google.gson.Gson
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch
import java.util.Calendar


const val ACTION_SET_TIMER = "com.example.autosilentapp.ACTION_SET_TIMER"
const val EXTRA_TIMER_ID = "com.example.autosilentapp.EXTRA_TIMER_ID"

class HomeFragment : Fragment(), TimeAdapter.OnTimeClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: TimeDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        database = TimeDB.getDatabase(requireContext())

        binding.btnFloating.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_homeFragment_to_addTimerFragment)
        }
        bindRecycleViewAndAdapter()
        swipeToDelete()

        getPrayerTimes()

        return binding.root
    }


    private fun getPrayerTimes() {
        val timeDao = database.prayerTimesDao()
        lifecycleScope.launch {
            val prayerTimes: PrayerTimesEntity = timeDao.getPrayerTimesById(1)
            setAlarmForPrayerTimes(prayerTimes)
        }
    }

    private fun setAlarmForPrayerTimes(prayerTimes: PrayerTimesEntity) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (prayerTimes != null) {
        // Set alarm for Fajr prayer time
        val fajrTimeParts = prayerTimes.fajr.split(":")
        val fajrHour = fajrTimeParts[0].toInt()
        val fajrMinute = fajrTimeParts[1].toInt()
        setAlarmForTime(fajrHour, fajrMinute, "Fajr")

        // Set alarm for Zuhr prayer time
        val zuhrTimeParts = prayerTimes.zuhr.split(":")
        val zuhrHour = zuhrTimeParts[0].toInt()
        val zuhrMinute = zuhrTimeParts[1].toInt()
        setAlarmForTime(zuhrHour, zuhrMinute, "Zuhr")

        // Set alarm for Asr prayer time
        val asrTimeParts = prayerTimes.asr.split(":")
        val asrHour = asrTimeParts[0].toInt()
        val asrMinute = asrTimeParts[1].toInt()
        setAlarmForTime(asrHour, asrMinute, "Asr")

        // Set alarm for Maghrib prayer time
        val maghribTimeParts = prayerTimes.maghrib.split(":")
        val maghribHour = maghribTimeParts[0].toInt()
        val maghribMinute = maghribTimeParts[1].toInt()
        setAlarmForTime(maghribHour, maghribMinute, "Maghrib")

        // Set alarm for Isha prayer time
        val ishaTimeParts = prayerTimes.isha.split(":")
        val ishaHour = ishaTimeParts[0].toInt()
        val ishaMinute = ishaTimeParts[1].toInt()
        setAlarmForTime(ishaHour, ishaMinute, "Isha")
        }
    }

    private fun setAlarmForTime(hour: Int, minute: Int, prayerName: String) {
        val context = requireContext()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a unique request code for the PendingIntent
        val requestCode = (hour * 100) + minute

        // Create an intent for the broadcast receiver
        val intent = Intent(context, SilentModeReceiver::class.java)
        intent.action = "com.example.autosilentapp.ACTION_SILENT_MODE"
        intent.putExtra("prayer_name", prayerName)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a calendar object and set the alarm time
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // Check if the alarm time is in the past, if so, add a day to the calendar
        val currentTime = System.currentTimeMillis()
        if (calendar.timeInMillis < currentTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Set the alarm using the AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        // Optionally, you can create a notification to display the upcoming alarm
        // You can customize the notification according to your app's design

    }



//    private fun setAlarmForPrayerTimes(prayerTimes: List<PrayerTimesEntity>) {
//        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val notificationManager =
//            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        for (prayerTime in prayerTimes) {
//            val timeParts = prayerTime.time.split(":")
//            val hour = timeParts[0].toInt()
//            val minute = timeParts[1].toInt()
//
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, hour)
//            calendar.set(Calendar.MINUTE, minute)
//            calendar.set(Calendar.SECOND, 0)
//
//            val pendingIntent = createPendingIntent(prayerTime.id)
//            alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//
//            // Show notification for each prayer time
//        }
//    }

    private fun createPendingIntent(prayerTimeId: Int): PendingIntent {
        val intent = Intent(requireContext(), SilentModeReceiver::class.java)
        intent.action = "com.example.autosilentapp.ACTION_SILENT_MODE"
        intent.putExtra("PRAYER_TIME_ID", prayerTimeId)
        return PendingIntent.getBroadcast(
            requireContext(),
            prayerTimeId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }




    private fun bindRecycleViewAndAdapter() {
        val adapter = TimeAdapter(this)
        binding.myRecycleView.adapter = adapter
        binding.myRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.myRecycleView.setHasFixedSize(true)

        lifecycleScope.launch {
            val getStoredTime: LiveData<List<TimeEntities>> = database.TimeDao().getAllTimes()
            getStoredTime.observe(viewLifecycleOwner, Observer { time ->
                adapter.setData(getStoredTime)
            })
        }
    }

    override fun onTimeClick(time: TimeEntities) {
        val bundle = Bundle()
        bundle.putString("time", Gson().toJson(time))
        findNavController().navigate(R.id.action_homeFragment_to_addTimerFragment, bundle)
    }

    private fun swipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val adapter = binding.myRecycleView.adapter as TimeAdapter
                val timeToDelete = adapter.getTime(position)
                // Cancel the alarm associated with the time to be deleted
                val pendingIntent = createPendingIntent(timeToDelete)
                val alarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                MyAlarmManager(requireContext()).cancelAlarms(pendingIntent, alarmManager)
                // Remove the item from the database and adapter
                lifecycleScope.launch {
                    database.TimeDao().deleteTime(timeToDelete)

                }
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                ).addSwipeLeftBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.purple_200
                    )
                ).addSwipeLeftActionIcon(R.drawable.delete_icon).addSwipeRightBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.purple_200)
                ).addSwipeRightActionIcon(R.drawable.delete_icon).create().decorate()
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.myRecycleView)
    }

    private fun createPendingIntent(time: TimeEntities): PendingIntent {
        val intent = Intent(requireContext(), SilentModeReceiver::class.java)
        intent.action = SilentModeReceiver.ACTION_SET_TIMER
        intent.putExtra(SilentModeReceiver.EXTRA_TIMER_ID, time.id)
        return PendingIntent.getBroadcast(
            requireContext(),
            time.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    override fun onResume() {
        super.onResume()
        val nm = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!nm.isNotificationPolicyAccessGranted) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }
}

