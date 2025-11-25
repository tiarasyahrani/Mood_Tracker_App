package com.nilam.moodtrackerapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// EXTENSION DataStore (wajib)
val Context.dataStore by preferencesDataStore(name = "settings_pref")

object ReminderPreferences {

    private val KEY_REMINDER_TIME = stringPreferencesKey("reminder_time")
    private val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")

    fun getReminderTime(context: Context): Flow<String> {
        return context.dataStore.data.map { pref ->
            pref[KEY_REMINDER_TIME] ?: "20:00"
        }
    }

    suspend fun saveReminderTime(context: Context, time: String) {
        context.dataStore.edit { pref ->
            pref[KEY_REMINDER_TIME] = time
        }
    }

    fun getReminderEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { pref ->
            pref[KEY_REMINDER_ENABLED] ?: false
        }
    }

    suspend fun saveReminderEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { pref ->
            pref[KEY_REMINDER_ENABLED] = enabled
        }
    }
    private val KEY_LANGUAGE = stringPreferencesKey("app_language")

    fun getLanguage(context: Context): Flow<String> {
        return context.dataStore.data.map { pref ->
            pref[KEY_LANGUAGE] ?: "en"
        }
    }

    suspend fun saveLanguage(context: Context, lang: String) {
        context.dataStore.edit { pref ->
            pref[KEY_LANGUAGE] = lang
        }
    }

}

