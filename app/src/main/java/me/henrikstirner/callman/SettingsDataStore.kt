package me.henrikstirner.callman

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingsDataStore {
	private val Context.dataStore by preferencesDataStore(name = "settings")

	val delayEnabled = booleanPreferencesKey("enable_delay")
	val narrationEnabled = booleanPreferencesKey("enable_narration")
	val timeoutEnabled = booleanPreferencesKey("enable_timeout")
	val headphonesConstraintEnabled = booleanPreferencesKey("enable_headphones_constraint")
	val bluetoothConnectionConstraintEnabled = booleanPreferencesKey("enable_bluetooth_connection_constraint")
	val ignoreUnknownNumbers = booleanPreferencesKey("ignore_unknown_numbers")
	val filterCalls = booleanPreferencesKey("filter_calls")
	val declineUnwantedCalls = booleanPreferencesKey("decline_unwanted_calls")
	val autostartEnabled = booleanPreferencesKey("enable_autostart")

	suspend fun setDelayEnable(context: Context, enabled: Boolean) {
		context.dataStore.edit { it[delayEnabled] = enabled }
	}

	fun getDelayEnabled(context: Context) : Flow<Boolean> {
		return context.dataStore.data.map { it[delayEnabled] ?: false }
	}
}