package me.henrikstirner.callman

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object SettingsDataStore {
	private val Context.dataStore by preferencesDataStore(name = "settings")
}