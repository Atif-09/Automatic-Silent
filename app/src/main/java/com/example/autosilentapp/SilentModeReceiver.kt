package com.example.autosilentapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class SilentModeReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SET_TIMER = "com.example.autosilentapp.ACTION_SET_TIMER"
        const val EXTRA_TIMER_ID = "com.example.autosilentapp.EXTRA_TIMER_ID"
    }
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SilentModeReceiver", "broadcast Received ")
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when(intent.action){
            "com.example.autosilentapp.ACTION_SILENT_MODE" ->{
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            }
            "com.example.autosilentapp.ACTION_RING_MODE" ->{
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        }
    }
}

