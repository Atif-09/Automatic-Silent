/*
import android.app.AlertDialog
import android.app.TimePickerDialog
import java.util.*

private fun startTimePickerDialog() {
    // Use the current time as the default values for the picker
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    // date picker dialog
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, endHour, endMinute ->
        c.set(Calendar.HOUR_OF_DAY, endHour)
        c.set(Calendar.MINUTE, endMinute)

        val amPm = if (c.get(Calendar.HOUR_OF_DAY) < 12) "AM" else "PM"
        val hourOfDay =
            if (c.get(Calendar.HOUR_OF_DAY) > 12) c.get(Calendar.HOUR_OF_DAY) - 12 else c.get(
                Calendar.HOUR_OF_DAY
            )
        val selectedTime = String.format("%02d:%02d %s", hourOfDay, endMinute, amPm)
        binding.setStartTime.text = selectedTime
    }

    val timePickerDialog = TimePickerDialog(
        requireContext(), AlertDialog.THEME_HOLO_LIGHT, timeSetListener, hour, minute,
        false
    )

    timePickerDialog.show()
}

*/
/*    fun startTimePickerDialog() {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        // date picker dialog
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, startHour, startMinute ->
            c.set(Calendar.HOUR_OF_DAY, startHour)
            c.set(Calendar.MINUTE, startMinute)
            val mSelectedTime = SimpleDateFormat("HH:mm").format(c.time)
            binding.setStartTime.text = mSelectedTime
        }
        val timePickerDialog = TimePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT, timeSetListener, hour, minute,
            false
        )
        timePickerDialog.show()
    }*//*


private fun endTimePickerDialog() {
    // Use the current time as the default values for the picker
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    // date picker dialog
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, endHour, endMinute ->
        c.set(Calendar.HOUR_OF_DAY, endHour)
        c.set(Calendar.MINUTE, endMinute)

        val amPm = if (c.get(Calendar.HOUR_OF_DAY) < 12) "AM" else "PM"
        val hourOfDay =
            if (c.get(Calendar.HOUR_OF_DAY) > 12) c.get(Calendar.HOUR_OF_DAY) - 12 else c.get(
                Calendar.HOUR_OF_DAY
            )
        val selectedTime = String.format("%02d:%02d %s", hourOfDay, endMinute, amPm)
        binding.setEndTime.text = selectedTime
    }

    val timePickerDialog = TimePickerDialog(
        requireContext(), AlertDialog.THEME_HOLO_LIGHT, timeSetListener, hour, minute,
        false
    )

    timePickerDialog.show()
}*/
