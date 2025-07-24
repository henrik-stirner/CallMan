package me.henrikstirner.callman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import me.henrikstirner.callman.ui.theme.CallManTheme

class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE)
    private val requestCode = 123

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, this.requestCode)
        }

        setContent {
            CallManTheme() {
                this.UI()
            }
        }
    }

    private fun startSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun toggleForegroundService() {
        val intent = Intent(this@MainActivity, ForegroundService::class.java)
        if (ForegroundService.instance == null) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun UIPreview() {
        CallManTheme(darkTheme = true) {
            this.UI()
        }
    }

    @Composable
    private fun UI() {
        Scaffold(
            topBar = {
                TopBar(onSettingsButtonClick = { this@MainActivity.startSettingsActivity() })
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BigRoundStartStopButton { this@MainActivity.toggleForegroundService() }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
	@Composable
    fun TopBar(onSettingsButtonClick: () -> Unit) {
        TopAppBar(
            title = { Text("") },
            actions = {
                IconButton(onClick = onSettingsButtonClick) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        )
    }

    @Composable
    fun BigRoundStartStopButton(onClick: () -> Unit) {
        var isActive by remember { mutableStateOf(false) }

        Button(
            onClick = {
                isActive = !isActive
                onClick()
            },
            shape = CircleShape,
            modifier = Modifier
                .size(200.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = if (isActive) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                contentDescription = if (isActive) "Stop" else "Start"
            )
        }
    }
}