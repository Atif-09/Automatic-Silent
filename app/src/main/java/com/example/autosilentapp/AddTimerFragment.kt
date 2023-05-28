package com.example.autosilentapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.autosilentapp.databinding.FragmentAddTimerBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.*


class AddTimerFragment : Fragment() {

    private lateinit var binding: FragmentAddTimerBinding
    private lateinit var database: TimeDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_timer, container, false)

        database = TimeDB.getDatabase(requireContext())

        getArgs()

        binding.startTimePicker.setOnClickListener {
            showTimePickerDialog(true)
        }
        binding.endTimePicker.setOnClickListener {
            showTimePickerDialog(false)
        }
        binding.btnSet.setOnClickListener {
            timeSelected()
        }

        return binding.root
    }

    private fun showTimePickerDialog(isStartTime: Boolean) {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // date picker dialog
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, min)

            val amPm = if (c.get(Calendar.HOUR_OF_DAY) < 12) "AM" else "PM"
            val hourOfDay =
                if (c.get(Calendar.HOUR_OF_DAY) > 12) c.get(Calendar.HOUR_OF_DAY) - 12 else c.get(
                    Calendar.HOUR_OF_DAY
                )
            val selectedTime = String.format("%02d:%02d %s", hourOfDay, min, amPm)

            // Check which button triggered the dialog and set the corresponding text
            if (isStartTime) {
                binding.setStartTime.text = selectedTime
            } else {
                binding.setEndTime.text = selectedTime
            }
        }
        val timePickerDialog = TimePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT, timeSetListener, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun timeSelected() {
        val getStartTime = binding.setStartTime.text.toString()
        val getEndTime = binding.setEndTime.text.toString()


        if (getStartTime.isNotEmpty() && getEndTime.isNotEmpty()) {
            if (getEndTime != getStartTime) {
                val jsonTime = arguments?.getString("time")
                val time = Gson().fromJson(jsonTime, TimeEntities::class.java)

                if (arguments?.containsKey("time") == true) {
                    lifecycleScope.launch {
                        database.TimeDao()
                            .updateTime(TimeEntities(time.id, getStartTime, getEndTime))
                    }
                } else {
                    lifecycleScope.launch {
                        val alarmManager = MyAlarmManager(requireContext())
                        database.TimeDao().insertTime(
                            TimeEntities(
                                startTime = getStartTime,
                                endTime = getEndTime
                            )
                        )
                        val recentTime = database.TimeDao().getRecentTime()
                        alarmManager.scheduleAlarms(recentTime.startTime,recentTime.endTime)
                    }
                }

                view?.findNavController()?.navigate(R.id.action_addTimerFragment2_to_homeFragment)
            } else {
                Toast.makeText(
                    context,
                    "Start Time And End Time Shouldn't Be Same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(context, "Select start and end time", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getArgs() {
        val jsonTime = arguments?.getString("time")
        if (jsonTime != null) {
            val time = Gson().fromJson(jsonTime, TimeEntities::class.java)
            time?.let {
                binding.setStartTime.text = it.startTime.toString()
                binding.setEndTime.text = it.endTime.toString()
            }
        }
    }
}

