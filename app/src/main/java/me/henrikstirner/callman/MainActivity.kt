package me.henrikstirner.callman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.SpannableString
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.henrikstirner.callman.ui.theme.CallManTheme

class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.READ_PHONE_STATE
    )
    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        }


        val button = Button(this)
        button.text = SpannableString("Start")
        button.setOnClickListener {
            val intent = Intent(this, ForegroundService::class.java)
            if (ForegroundService.instance == null) {
                button.text = SpannableString("Stop")
                startService(intent)
            } else {
                button.text = SpannableString("Start")
                stopService(intent)
            }
        }

        setContentView(button)
    }

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}