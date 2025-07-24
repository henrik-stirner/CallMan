package me.henrikstirner.callman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class CallHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return  // prevent spoofing

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (TelephonyManager.EXTRA_STATE_RINGING == state) {
            ForegroundService.instance?.tryAcceptCall()
                ?: Log.w("CallReceiver", "Service not running or instance is null")
        }
    }
}