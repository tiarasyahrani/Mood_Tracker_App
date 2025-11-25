package com.nilam.moodtrackerapp

import androidx.annotation.DrawableRes

@DrawableRes
fun getMoodIcon(mood: String): Int {
    return when (mood) {
        "😄" -> R.drawable.happy
        "😐" -> R.drawable.neutral
        "😢" -> R.drawable.sad
        "😡" -> R.drawable.angry
        "😴" -> R.drawable.sleepy
        else -> R.drawable.mood_default
    }
}
