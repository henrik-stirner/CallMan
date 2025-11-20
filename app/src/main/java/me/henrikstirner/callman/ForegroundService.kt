package me.henrikstirner.callman

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.telecom.TelecomManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {
    private val CHANNEL_ID = "call_manager"

    companion object {
        var instance: ForegroundService? = null
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Call Manager",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = "TOGGLE_ACTIVE"
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Call Manager")
            .setContentText("Waiting for incoming calls...")
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, createNotification())
        Log.d("ForegroundService", "Foreground service started")

        return START_STICKY
    }

    fun tryAcceptCall() {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("ForegroundService", "Accepting call...")
            telecomManager.acceptRingingCall()
        } else {
            Log.w("ForegroundService", "Cannot accept call, permission ANSWER_PHONE_CALLS not granted")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}