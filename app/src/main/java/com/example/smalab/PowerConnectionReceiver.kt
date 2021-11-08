package com.example.smalab

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.smalab.MainActivity.Companion.CHANNEL_ID

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var statusMessage = ""

        val chargePlug = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

        when {
            usbCharge -> statusMessage = "Charging USB"
            acCharge -> statusMessage = "Charging AC"
            else -> statusMessage = "Not Charging"
        }

        (context as MainActivity).batteryStatusTextView.text = statusMessage

        if(context.isFinishing) {
            val newIntent = Intent(context, MainActivity::class.java)
            newIntent.putExtra("status", statusMessage)

            val pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "battery channel name", importance).apply {
                description = "battery channel description"
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val mBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Charging Status Changed")
                    .setContentText(statusMessage)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            notificationManager.notify(MainActivity.notificationId, mBuilder.build())
        }
    }
}