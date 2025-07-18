package me.henrikstirner.callman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // prevent spoofing
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        Log.d("CallReceiver", "Phone state: $state")

        if (TelephonyManager.EXTRA_STATE_RINGING == state) {
            /*
            Log.d("CallReceiver", "Incoming call detected. Attempting to answer...")

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // send KeyEvent
            val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK)
            val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK)

            audioManager.dispatchMediaKeyEvent(downEvent)
            audioManager.dispatchMediaKeyEvent(upEvent)

            Log.d("CallReceiver", "Sent headset hook key events.")
            */
            ForegroundService.instance?.tryAcceptCall()
                ?: Log.w("CallReceiver", "Service not running or instance is null")
        }
    }
}