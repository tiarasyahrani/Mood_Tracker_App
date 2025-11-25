package com.nilam.moodtrackerapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.nilam.moodtrackerapp.MoodEntry

class MoodViewModel : ViewModel() {
    val moods = mutableStateListOf<MoodEntry>()

    fun addMood(mood: MoodEntry) {
        moods.add(0, mood) // terbaru di atas
    }

    fun updateNote(date: String, newNote: String) {
        val index = moods.indexOfFirst { it.date == date }
        if (index != -1) {
            moods[index] = moods[index].copy(note = newNote)
        }
    }

}
