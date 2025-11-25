package com.nilam.moodtrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nilam.moodtrackerapp.R
import com.nilam.moodtrackerapp.viewmodel.MoodViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(viewModel: MoodViewModel) {
    val currentMonth = YearMonth.now()
    val totalDays = currentMonth.lengthOfMonth()

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(
                id = R.string.calendar_title,
                currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() },
                currentMonth.year
            ),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(350.dp)
        ) {
            items((1..totalDays).toList()) { day ->
                val date = LocalDate.of(currentMonth.year, currentMonth.month, day)
                val dateString = date.toString()
                val moodToday = viewModel.moods.find { it.date == dateString }

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .background(
                            when (moodToday?.mood) {
                                "😄" -> Color(0xFFB2FF59) // Hijau
                                "😐" -> Color(0xFFFFF59D) // Kuning
                                "😢" -> Color(0xFF81D4FA) // Biru
                                "😡" -> Color(0xFFFF8A80) // Merah
                                "😴" -> Color(0xFFD7CCC8) // Abu
                                else -> Color(0xFFE0E0E0) // Default abu muda
                            }
                        )
                        .clickable { selectedDate = date }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day.toString())
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate?.let { date ->
            val mood = viewModel.moods.find { it.date == date.toString() }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.date_label, date.toString()),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = stringResource(id = R.string.mood_label, mood?.mood ?: "-"))
                    Text(text = stringResource(id = R.string.note_label, mood?.note ?: "-"))
                }
            }
        }
    }
}
