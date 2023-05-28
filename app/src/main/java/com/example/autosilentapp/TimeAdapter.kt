package com.example.autosilentapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.autosilentapp.databinding.CustomListTimeBinding


class TimeAdapter(private val listener: OnTimeClickListener) :
    RecyclerView.Adapter<TimeAdapter.ViewHolder>() {

    private var timeList = listOf<TimeEntities>()

    class ViewHolder(val binding: CustomListTimeBinding) : RecyclerView.ViewHolder(binding.root)


    interface OnTimeClickListener {
        fun onTimeClick(time: TimeEntities)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CustomListTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTime = timeList[position]



        if (currentTime.startTime != currentTime.endTime) {
            holder.binding.setListStartTime.text = currentTime.startTime.toString()
            holder.binding.setListEndTime.text = currentTime.endTime.toString()
        } else {
            fun Context.toast(message: CharSequence) =
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener {
            listener.onTimeClick(currentTime)
        }
    }


    override fun getItemCount(): Int {
        return timeList.size
    }

    fun setData(time: LiveData<List<TimeEntities>>) {
        time.observeForever { newData ->
            timeList = newData
            notifyDataSetChanged()
        }
    }

    fun getTime(position: Int): TimeEntities {
        return timeList[position]
    }
}
