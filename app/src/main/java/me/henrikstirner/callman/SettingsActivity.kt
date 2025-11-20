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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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
        val preferencesDataStore = remember(context) { PreferencesDataStore(context) }

        val delayEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.DELAY_ENABLED, false).collectAsState(initial = false)
        val delayMillis by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.DELAY_MILLIS, 3_000).collectAsState(initial = 3000)
        val narrationEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.NARRATION_ENABLED, false).collectAsState(initial = false)
        val timeoutEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.TIMEOUT_ENABLED, false).collectAsState(initial = false)
        val timeoutMillis by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.TIMEOUT_MILLIS, 3_600_000).collectAsState(initial = 3_600_000)
        val headphonesConstraintEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.HEADPHONES_CONSTRAINT_ENABLED, false).collectAsState(initial = false)
        val bluetoothConnectionConstraintEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.BLUETOOTH_CONNECTION_CONSTRAINT_ENABLED, false).collectAsState(initial = false)
        val ignoreUnknownNumbers by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.IGNORE_UNKNOWN_NUMBERS, false).collectAsState(initial = false)
        val filterCalls by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.FILTER_CALLS, false).collectAsState(initial = false)
        val declineUnwantedCalls by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.DECLINE_UNWANTED_CALLS, false).collectAsState(initial = false)
        val autostartEnabled by preferencesDataStore.getPreferenceFlow(PreferencesDataStore.AUTOSTART_ENABLED, false).collectAsState(initial = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingTitle("General")
            // General
            SettingSwitch("Wait before accepting calls", delayEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.DELAY_ENABLED, it) }
            }
            SettingNumberEntry("Delay", delayMillis) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.DELAY_MILLIS, it) }  // TODO
            }  // TODO
            SettingSwitch("Narration", narrationEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.NARRATION_ENABLED, it) }
            }
            SettingSwitch("Stop automatically", timeoutEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.TIMEOUT_ENABLED, it) }
            }
            SettingNumberEntry("Timeout", timeoutMillis) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.TIMEOUT_MILLIS, it) } }  // TODO
            // --------
            SettingSpacer()

            SettingTitle("Constraints")
            // Connectivity Constraints
            SettingSwitch("Headphones connected", headphonesConstraintEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.HEADPHONES_CONSTRAINT_ENABLED, it) }
            }
            SettingSwitch("Bluetooth headset connected", bluetoothConnectionConstraintEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.BLUETOOTH_CONNECTION_CONSTRAINT_ENABLED, it) }
            }
            SettingButton("Specify Device") {  }  // TODO
            // Call-Specific Constraints
            SettingSwitch("Ignore unknown numbers", ignoreUnknownNumbers) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.IGNORE_UNKNOWN_NUMBERS, it) }
            }
            SettingSwitch("Filter calls", filterCalls) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.FILTER_CALLS, it) }
            }
            SettingButton("Configure Filter") { this@SettingsActivity.startCallFilterActivity() }  // TODO
            // --------
            SettingSpacer()

            SettingTitle("Miscellaneous")
            //Miscellaneous
            SettingSwitch("Decline unwanted calls", declineUnwantedCalls) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.DECLINE_UNWANTED_CALLS, it) }
            }
            SettingSwitch("Launch on system startup", autostartEnabled) {
                scope.launch { preferencesDataStore.setPreference(PreferencesDataStore.AUTOSTART_ENABLED, it) }
            }
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
    fun SettingSwitch(label: String, isChecked: Boolean, onClick: (it: Boolean) -> Unit) {
        Row(
            modifier = Modifier
                .clickable { onClick(isChecked) }  // whole row clickable
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = isChecked,
                onCheckedChange = {
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

    private val fullTimeRegex = Regex("^\\s*(\\d+\\s*[dhms]\\s*)+\$")

    fun String.isValidTimeFormat(): Boolean = fullTimeRegex.matches(this)

    private val timeRegex = Regex("(\\d+)\\s*([dhms])")

    fun String.toMillis(): Long {
        var total = 0L
        for ((value, unit) in timeRegex.findAll(this).map { it.groupValues[1].toLong() to it.groupValues[2] }) {
            total += when (unit) {
                "d" -> value * 86_400_000
                "h" -> value * 3_600_000
                "m" -> value * 60_000
                "s" -> value * 1_000
                else -> 0
            }
        }
        return total
    }

    fun Long.toTimeString(): String {
        var ms = this
        val d = ms / 86_400_000; ms %= 86_400_000
        val h = ms / 3_600_000;  ms %= 3_600_000
        val m = ms / 60_000;     ms %= 60_000
        val s = ms / 1_000

        return buildList {
            if (d > 0) add("${d}d")
            if (h > 0) add("${h}h")
            if (m > 0) add("${m}m")
            if (s > 0 || isEmpty()) add("${s}s")
        }.joinToString(" ")
    }

    @Composable
    fun SettingNumberEntry(label: String, initialValue: Long, onInput: (it: Long) -> Unit) {
        var value by remember { mutableStateOf(initialValue.toTimeString()) }

        LaunchedEffect(initialValue) {
            val formatted = initialValue.toTimeString()
            if (value != formatted) value = formatted
        }

        val scope = rememberCoroutineScope()
        var debounceJob by remember { mutableStateOf<Job?>(null) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = value,
            singleLine = true,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            /*
            keyboardActions = KeyboardActions(
                onDone = {
                    if (value.isNotEmpty() && value.all(Char::isDigit)) {
                        onInput(value.toLong())
                    }
                }
            ),
            */
            onValueChange = { it: String ->
                value = it
                debounceJob?.cancel()

                if (it.isEmpty()) return@OutlinedTextField

                debounceJob = scope.launch {
                    delay(1000)

                    if (it.isValidTimeFormat()) {
                        onInput(it.toMillis())
                    } else if (it.all(Char::isDigit)) {
                        onInput(it.toLong() * 1000)
                    }
                }
            },
            isError = value.isNotEmpty() && !value.all(Char::isDigit) && !value.isValidTimeFormat()
        )
    }
}
