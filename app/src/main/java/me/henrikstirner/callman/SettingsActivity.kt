package me.henrikstirner.callman

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.henrikstirner.callman.ui.theme.CallManTheme

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallManTheme {
                this.UI()
            }
        }
    }

    private fun startCallFilterActivity() {
        val intent = Intent(this, CallFilterActivity::class.java)
        startActivity(intent)
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
            topBar = { TopBar(onBackButtonClick = { this@SettingsActivity.finish() }) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Settings()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(onBackButtonClick: () -> Unit) {
        CenterAlignedTopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }

    @Composable
    fun Settings() {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val delayEnabledFlow = remember { SettingsDataStore.getDelayEnabled(context) }
        val delayEnabled by delayEnabledFlow.collectAsState(initial = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {


            SettingTitle("General")
            // General
            SettingSwitch("Wait before accepting calls", delayEnabled) { scope.launch { SettingsDataStore.setDelayEnable(context, it) } }
            SettingNumberEntry("Delay") {  }
            SettingSwitch("Narration", false) { }
            SettingSwitch("Stop automatically", false) { }
            SettingNumberEntry("Timeout") {  }
            // --------
            SettingSpacer()

            SettingTitle("Constraints")
            // Connectivity Constraints
            SettingSwitch("Headphones connected", false) { }
            SettingSwitch("Bluetooth headset connected", false) { }
            SettingButton("Specify Device") {  }
            // Call-Specific Constraints
            SettingSwitch("Ignore unknown numbers", false) { }
            SettingSwitch("Filter calls", false) {  }
            SettingButton("Configure Filter") { this@SettingsActivity.startCallFilterActivity() }
            // --------
            SettingSpacer()

            SettingTitle("Miscellaneous")
            //Miscellaneous
            SettingSwitch("Decline unwanted calls", false) { }
            SettingSwitch("Launch on system startup", false) { }
            // --------
            SettingSpacer()
        }
    }

    @Composable
    fun SettingTitle(label: String) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }

    @Composable
    fun SettingSpacer() {
        Spacer(modifier = Modifier.size(size = 24.dp))
    }

    @Composable
    fun SettingSwitch(label: String, initialState: Boolean, onClick: (it: Boolean) -> Unit) {
        var isChecked by remember { mutableStateOf(initialState) }

        Row(
            modifier = Modifier
                .clickable { isChecked = !isChecked }  // whole row clickable
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onClick(it)
                }
            )
            Spacer(modifier = Modifier.size(size = 16.dp))
            Text(text=label)
        }
    }

    @Composable
    fun SettingButton(label: String, onClick: () -> Unit) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            onClick = { onClick() }
        ) {
            Text(text=label)
        }
    }

    @Composable
    fun SettingNumberEntry(label: String, onInput: (it: Int) -> Unit) {
        var input by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = input,
            singleLine = true,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = { it ->
                if (it.all { it.isDigit() }) {
                    input = it
                    if (!it.isEmpty()) {
                        onInput(it.toInt())
                    }
                }
            }
        )
    }
}
