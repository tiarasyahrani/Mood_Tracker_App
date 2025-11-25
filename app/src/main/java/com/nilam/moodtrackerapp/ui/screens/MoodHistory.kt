package com.nilam.moodtrackerapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.nilam.moodtrackerapp.R
import com.nilam.moodtrackerapp.viewmodel.MoodViewModel

@Composable
fun MoodHistory(viewModel: MoodViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(viewModel.moods) { mood ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = stringResource(id = R.string.history_date, mood.date))
                    Text(text = stringResource(id = R.string.history_mood, mood.mood))
                    mood.note?.let { note ->
                        Text(text = stringResource(id = R.string.history_note, note))
                    }
                }
            }
        }
    }
}
