package com.example.smalab

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var batteryStatusTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryStatusTextView = findViewById(R.id.battery_status_textView)

        val receiver = PowerConnectionReceiver()

        IntentFilter(Intent.ACTION_BATTERY_CHANGED).also {
            registerReceiver(receiver, it)
        }
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val notificationId = 123
    }
}