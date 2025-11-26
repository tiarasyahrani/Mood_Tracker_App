package com.nilam.moodtrackerapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nilam.moodtrackerapp.MoodEntry
import com.nilam.moodtrackerapp.R
import com.nilam.moodtrackerapp.getMoodIcon
import com.nilam.moodtrackerapp.viewmodel.MoodViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MoodBoardScreen(moodViewModel: MoodViewModel) {

    val moods = remember { moodViewModel.moods.sortedBy { it.date } }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation ==
            android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F1FF))
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.board_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // ======================
        // LANDSCAPE MODE
        // ======================
        if (isLandscape) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {

                // ----- Mood Flow kiri -----
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(stringResource(id = R.string.board_mood_flow), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        LineChartWithDates(moods)
                    }
                }

                Spacer(Modifier.width(16.dp))

                // ----- Mood Bar kanan -----
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(stringResource(id = R.string.board_mood_bar), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        MoodPercentageBar(moods)
                    }
                }
            }

        } else {
            // ======================
            // PORTRAIT MODE
            // ======================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(id = R.string.board_mood_flow), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    LineChartWithDates(moods)
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(id = R.string.board_mood_bar), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    MoodPercentageBar(moods)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            stringResource(id = R.string.board_timeline),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        moods.forEach { item ->
            TimelineItem(
                date = item.date,
                mood = item.mood,
                note = item.note.orEmpty()
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

//////////////////////////////////////////////////////////////
// LINE CHART + TANGGAL
//////////////////////////////////////////////////////////////

@Composable
fun LineChartWithDates(moods: List<MoodEntry>) {

    if (moods.size < 2) {
        Text(stringResource(id = R.string.board_not_enough_data))
        return
    }

    val values = moods.map { moodToInt(it.mood) }
    val dates = moods.map { formatDateShort(it.date) }

    Column {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {

            val maxY = 5f
            val minY = 1f
            val stepX = size.width / (values.size - 1)

            val points = values.mapIndexed { index, value ->
                val norm = (value - minY) / (maxY - minY)
                val y = size.height - (norm * size.height)
                Offset(index * stepX, y)
            }

            drawPath(
                path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (p in points.drop(1)) lineTo(p.x, p.y)
                },
                color = Color(0xFF0A63FF),
                style = Stroke(width = 6f)
            )

            points.forEach {
                drawCircle(Color(0xFF0A63FF), radius = 10f, center = it)
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dates.forEach { d ->
                Text(d, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

//////////////////////////////////////////////////////////////
// MOOD PERCENTAGE BAR
//////////////////////////////////////////////////////////////

@Composable
fun MoodPercentageBar(moods: List<MoodEntry>) {

    val total = (moods.size.takeIf { it > 0 } ?: 1).toFloat()

    val happy = moods.count { it.mood == "😄" } / total
    val neutral = moods.count { it.mood == "😐" } / total
    val sad = moods.count { it.mood == "😢" } / total
    val angry = moods.count { it.mood == "😡" } / total
    val sleepy = moods.count { it.mood == "😴" } / total

    val gradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFB2FF59),
            Color(0xFFFFF59D),
            Color(0xFF81D4FA),
            Color(0xFFFF8A80),
            Color(0xFFD7CCC8)
        )
    )

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(gradient, RoundedCornerShape(20.dp))
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(
                id = R.string.board_percentage_format,
                "%.0f".format(happy * 100),
                "%.0f".format(neutral * 100),
                "%.0f".format(sad * 100),
                "%.0f".format(angry * 100),
                "%.0f".format(sleepy * 100)
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

//////////////////////////////////////////////////////////////
// TIMELINE ITEM
//////////////////////////////////////////////////////////////

@Composable
fun TimelineItem(date: String, mood: String, note: String) {

    val iconRes = getMoodIcon(mood)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = mood,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(formatDateLong(date), fontWeight = FontWeight.Bold)
            Text(stringResource(id = R.string.board_mood_label, mood))
            if (note.isNotEmpty()) Text(note, style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

//////////////////////////////////////////////////////////////
// HELPERS
//////////////////////////////////////////////////////////////

fun moodToInt(mood: String): Float {
    return when (mood) {
        "😄" -> 5f
        "😐" -> 3f
        "😢" -> 2f
        "😡" -> 1f
        "😴" -> 2.5f
        else -> 3f
    }
}

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

fun formatDateShort(date: String): String {
    return try {
        LocalDate.parse(date).format(DateTimeFormatter.ofPattern("dd"))
    } catch (e: Exception) {
        "--"
    }
}

fun formatDateLong(date: String): String {
    return try {
        LocalDate.parse(date).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: Exception) {
        "--"
    }
}
