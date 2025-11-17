package me.henrikstirner.callman

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesDataStore(private val context: Context) {
    companion object {
        val DELAY_ENABLED = booleanPreferencesKey("enable_delay")
        val NARRATION_ENABLED = booleanPreferencesKey("enable_narration")
        val TIMEOUT_ENABLED = booleanPreferencesKey("enable_timeout")
        val HEADPHONES_CONSTRAINT_ENABLED = booleanPreferencesKey("enable_headphones_constraint")
        val BLUETOOTH_CONNECTION_CONSTRAINT_ENABLED = booleanPreferencesKey("enable_bluetooth_connection_constraint")
        val IGNORE_UNKNOWN_NUMBERS = booleanPreferencesKey("ignore_unknown_numbers")
        val FILTER_CALLS = booleanPreferencesKey("filter_calls")
        val DECLINE_UNWANTED_CALLS = booleanPreferencesKey("decline_unwanted_calls")
        val AUTOSTART_ENABLED = booleanPreferencesKey("enable_autostart")
    }

    fun <T> getPreferenceFlow(key: Preferences.Key<T>, default: T): Flow<T> {
        return context.dataStore.data.map { prefs -> prefs[key] ?: default }
    }

    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs -> prefs[key] = value }
    }
}
