package com.nilam.moodtrackerapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nilam.moodtrackerapp.MoodEntry
import com.nilam.moodtrackerapp.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddMoodScreen(
    onSave: (MoodEntry) -> Unit,
    onBack: () -> Unit
) {
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }

    val moodList = listOf("😄", "😐", "😢", "😡", "😴")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xC4043E6E),
                        Color(0xFFFFFFFF),
                    )
                )
            )
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ================= TOP BAR =================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }

                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                    fontSize = 22.sp,
                    color = Color.White
                )

                IconButton(
                    onClick = {
                        if (selectedMood != null) {
                            onSave(
                                MoodEntry(
                                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                                    mood = selectedMood!!,
                                    note = note,
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = stringResource(R.string.save),
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ================= MAIN CARD =================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        stringResource(R.string.how_was_your_day),
                        fontSize = 18.sp,
                        color = Color(0xFF1A1A1A)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Mood Picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        moodList.forEach { iconName ->

                            val resId = when (iconName) {
                                "😄" -> R.drawable.happy
                                "😐" -> R.drawable.neutral
                                "😢" -> R.drawable.sad
                                "😡" -> R.drawable.angry
                                "😴" -> R.drawable.sleepy
                                else -> null
                            }

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        color = if (selectedMood == iconName)
                                            Color(0xFFE3F2FD)
                                        else
                                            Color(0xFFF7FBFF),
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = if (selectedMood == iconName) 2.dp else 1.dp,
                                        color = if (selectedMood == iconName)
                                            Color(0xFF42A5F5)
                                        else
                                            Color.Gray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedMood = iconName },
                                contentAlignment = Alignment.Center
                            ) {
                                resId?.let { id ->
                                    Image(
                                        painter = painterResource(id = id),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ================= NOTE INPUT =================
                    Text(
                        stringResource(R.string.write_about_today),
                        fontSize = 18.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text(stringResource(R.string.write_something)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        }
    }
}
