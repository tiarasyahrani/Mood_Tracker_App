package com.nilam.moodtrackerapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.nilam.moodtrackerapp.R
import com.nilam.moodtrackerapp.viewmodel.MoodViewModel
import com.nilam.moodtrackerapp.getMoodIcon
import com.nilam.moodtrackerapp.ui.components.SparkleGlitterBackground

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    moodViewModel: MoodViewModel,
    onAdd: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation ==
            android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val today = LocalDate.now()
    var shownMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var editNoteTemp by remember { mutableStateOf("") }

    val firstOfMonth = shownMonth.atDay(1)
    val lastDay = shownMonth.lengthOfMonth()
    val firstWeekday = firstOfMonth.dayOfWeek.value % 7

    val cells = buildList {
        repeat(firstWeekday) { add(null) }
        for (d in 1..lastDay) add(shownMonth.atDay(d))
    }

    // Mood hari ini → background
    val moodToday = moodViewModel.moods.find { it.date == today.toString() }?.mood


    // ================================
    // PORTRAIT MODE
    // ================================
    if (!isLandscape) {
        Box(Modifier.fillMaxSize()) {

            // 🌟 Sparkle Glitter Background
            SparkleGlitterBackground(
                mood = moodToday,
                modifier = Modifier.matchParentSize()
            )

            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { onAdd() },
                        containerColor = Color(0xFF0277BD),
                        contentColor = Color.White
                    ) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_mood)) }
                },
                floatingActionButtonPosition = FabPosition.Center,
                containerColor = Color.Transparent
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { shownMonth = shownMonth.minusMonths(1) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.previous_month))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = shownMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF01579B)
                            )
                            Text(
                                text = today.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        IconButton(onClick = { shownMonth = shownMonth.plusMonths(1) }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = stringResource(R.string.next_month))
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // WEEKDAY NAMES
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf(
                            stringResource(R.string.sun),
                            stringResource(R.string.mon),
                            stringResource(R.string.tue),
                            stringResource(R.string.wed),
                            stringResource(R.string.thu),
                            stringResource(R.string.fri),
                            stringResource(R.string.sat)
                        ).forEach {
                            Text(
                                it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    // CALENDAR GRID
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 260.dp, max = 340.dp),
                        contentPadding = PaddingValues(0.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(cells) { date ->
                            if (date == null) {
                                Box(Modifier.size(48.dp))
                            } else {
                                CalendarDayCell(
                                    date = date,
                                    mood = moodViewModel.moods.find { it.date == date.toString() }?.mood,
                                    isSelected = selectedDate == date,
                                    onClick = { selectedDate = date }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // DETAIL CARD
                    val sel = selectedDate ?: today
                    val selMood = moodViewModel.moods.find { it.date == sel.toString() }

                    MoodRecordCard(
                        date = sel,
                        mood = selMood?.mood,
                        note = selMood?.note,
                        onEdit = {
                            editNoteTemp = selMood?.note ?: ""
                            showEditDialog = true
                        }
                    )

                    Spacer(Modifier.height(60.dp))

                    if (showEditDialog) {
                        EditNoteDialog(
                            onDismiss = { showEditDialog = false },
                            onSave = {
                                moodViewModel.updateNote(sel.toString(), editNoteTemp)
                                showEditDialog = false
                            },
                            editNoteTemp = editNoteTemp,
                            onChange = { editNoteTemp = it }
                        )
                    }
                }
            }
        }
        return
    }


    // ================================
    // LANDSCAPE MODE
    // ================================
    Box(Modifier.fillMaxSize()) {

        SparkleGlitterBackground(
            mood = moodToday,
            modifier = Modifier.matchParentSize()
        )

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onAdd() },
                    containerColor = Color(0xFF0277BD),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_mood))
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = Color.Transparent
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // LEFT CALENDAR
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { shownMonth = shownMonth.minusMonths(1) }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = shownMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF01579B)
                            )
                            Text(
                                text = today.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        IconButton(onClick = { shownMonth = shownMonth.plusMonths(1) }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }

                    Spacer(Modifier.height(10.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(cells) { date ->
                            if (date == null) {
                                Box(Modifier.size(48.dp))
                            } else {
                                CalendarDayCell(
                                    date = date,
                                    mood = moodViewModel.moods.find { it.date == date.toString() }?.mood,
                                    isSelected = selectedDate == date,
                                    onClick = { selectedDate = date }
                                )
                            }
                        }
                    }
                }

                // RIGHT DETAIL
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    val sel = selectedDate ?: today
                    val selMood = moodViewModel.moods.find { it.date == sel.toString() }

                    MoodRecordCard(
                        date = sel,
                        mood = selMood?.mood,
                        note = selMood?.note,
                        onEdit = {
                            editNoteTemp = selMood?.note ?: ""
                            showEditDialog = true
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    if (showEditDialog) {
                        EditNoteDialog(
                            onDismiss = { showEditDialog = false },
                            onSave = {
                                moodViewModel.updateNote(sel.toString(), editNoteTemp)
                                showEditDialog = false
                            },
                            editNoteTemp = editNoteTemp,
                            onChange = { editNoteTemp = it }
                        )
                    }
                }
            }
        }
    }
}


// ================================================================
// SUPPORTING COMPONENTS
// ================================================================

@Composable
private fun EditNoteDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    editNoteTemp: String,
    onChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.edit_note)) },
        text = {
            OutlinedTextField(
                value = editNoteTemp,
                onValueChange = { onChange(it) },
                label = { Text(stringResource(R.string.your_note)) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave() }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text(stringResource(R.string.cancel)) }
        }
    )
}


@Composable
private fun CalendarDayCell(
    date: LocalDate,
    mood: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFB0BEC5).copy(alpha = if (isSelected) 0.5f else 0.3f))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(
                    id = if (mood == null) R.drawable.mood_default else getMoodIcon(mood)
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF37474F)
        )
    }
}


@Composable
private fun MoodRecordCard(date: LocalDate, mood: String?, note: String?, onEdit: () -> Unit) {

    val moodIconRes = when (mood) {
        "😄" -> R.drawable.happy
        "😐" -> R.drawable.neutral
        "😢" -> R.drawable.sad
        "😡" -> R.drawable.angry
        "😴" -> R.drawable.sleepy
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(containerColor = Color(0xCCFFFFFF))
    ) {

        Column(Modifier.padding(12.dp)) {

            Text(
                stringResource(
                    R.string.date_display,
                    date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (moodIconRes != null) {
                    Image(
                        painter = painterResource(moodIconRes),
                        contentDescription = mood,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }

                Text(
                    text = if (mood != null)
                        stringResource(R.string.mood_display, mood)
                    else
                        stringResource(R.string.no_record),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!note.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(note, style = MaterialTheme.typography.bodySmall)
            } else {
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(R.string.tap_add_note),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
