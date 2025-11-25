package com.nilam.moodtrackerapp
import android.net.Uri

data class MoodEntry(
    val date: String,
    val mood: String,
    val note: String? = null,
)

