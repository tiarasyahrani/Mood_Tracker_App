package com.nilam.moodtrackerapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    private const val PREF_NAME = "app_language_pref"
    private const val KEY_LANGUAGE = "selected_language"

    // =====================================================
    // Save language pilihan user
    // =====================================================
    fun saveLanguage(context: Context, lang: String) {
        val prefs = getPrefs(context)
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
    }

    // =====================================================
    // Ambil bahasa terakhir yang tersimpan
    // =====================================================
    fun getSavedLanguage(context: Context): String {
        val prefs = getPrefs(context)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    // =====================================================
    // Apply bahasa ke seluruh app
    // =====================================================
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    // =====================================================
    // Helper untuk SharedPreferences
    // =====================================================
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
