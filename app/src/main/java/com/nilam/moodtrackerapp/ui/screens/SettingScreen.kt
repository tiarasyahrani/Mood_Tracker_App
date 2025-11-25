package com.nilam.moodtrackerapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.nilam.moodtrackerapp.data.ReminderPreferences
import com.nilam.moodtrackerapp.utils.ReminderScheduler
import com.nilam.moodtrackerapp.MainActivity
import com.nilam.moodtrackerapp.utils.LocaleHelper
import com.nilam.moodtrackerapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDarkTheme by remember { mutableStateOf(false) }

    // === REMINDER STATE ===
    val savedTime by ReminderPreferences.getReminderTime(context)
        .collectAsState(initial = "20:00")

    var reminderTime by remember(savedTime) { mutableStateOf(savedTime) }

    val savedEnabled by ReminderPreferences.getReminderEnabled(context)
        .collectAsState(initial = false)

    var reminderEnabled by remember(savedEnabled) { mutableStateOf(savedEnabled) }
    var showTimePicker by remember { mutableStateOf(false) }

    // === LANGUAGE ===
    var showLanguageDialog by remember { mutableStateOf(false) }
    val currentLanguage = LocaleHelper.getSavedLanguage(context)

    fun changeLanguage(lang: String) {
        LocaleHelper.saveLanguage(context, lang)
        LocaleHelper.setLocale(context, lang)

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.settings),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        SectionTitle(stringResource(R.string.general))

        // === REMINDER SWITCH ===
        SettingItemSwitch(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.reminder_time),
            checked = reminderEnabled,
            value = reminderTime,
            onCheckedChange = {
                reminderEnabled = it

                scope.launch {
                    ReminderPreferences.saveReminderEnabled(context, it)
                }

                if (it) showTimePicker = true
            }
        )

        // Backup
        SettingItem(
            icon = Icons.Default.Cloud,
            title = stringResource(R.string.backup_restore),
            value = "",
            onClick = {}
        )

        // Start of week
        SettingItem(
            icon = Icons.Default.CalendarToday,
            title = stringResource(R.string.start_week),
            value = "Sunday",
            onClick = {}
        )

        // === LANGUAGE ===
        SettingItem(
            icon = Icons.Default.Language,
            title = stringResource(R.string.language),
            value = currentLanguage.uppercase(),
            onClick = { showLanguageDialog = true }
        )

        // Theme (dummy)
        SettingItemSwitch(
            icon = Icons.Default.Image,
            title = stringResource(R.string.change_theme),
            checked = isDarkTheme,
            onCheckedChange = { isDarkTheme = it }
        )

        Spacer(Modifier.height(24.dp))

        SectionTitle(stringResource(R.string.other))

        SettingItem(
            icon = Icons.Default.Security,
            title = stringResource(R.string.privacy_policy),
            value = "",
            onClick = {}
        )

        SettingItem(
            icon = Icons.Default.Description,
            title = stringResource(R.string.terms_conditions),
            value = "",
            onClick = {}
        )

        // === TIME PICKER ===
        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = reminderTime.substringBefore(":").toInt(),
                initialMinute = reminderTime.substringAfter(":").toInt()
            )

            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute

                        reminderTime = "%02d:%02d".format(hour, minute)
                        showTimePicker = false

                        scope.launch {
                            ReminderPreferences.saveReminderTime(context, reminderTime)
                        }

                        // === PANGGIL VERSI BARU TANPA PARAMETER ===
                        ReminderScheduler.scheduleDailyReminder(context)

                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }

        // === LANGUAGE DIALOG ===
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text(stringResource(R.string.language)) },
                confirmButton = {},
                text = {
                    Column {
                        Text(
                            stringResource(R.string.english),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    changeLanguage("en")
                                    showLanguageDialog = false
                                }
                                .padding(12.dp)
                        )
                        Text(
                            stringResource(R.string.indonesian),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    changeLanguage("id")
                                    showLanguageDialog = false
                                }
                                .padding(12.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0D47A1),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF37474F))
                Spacer(Modifier.width(12.dp))
                Text(title, fontSize = 16.sp)
            }

            Spacer(Modifier.weight(1f))

            if (value.isNotEmpty()) {
                Text(value, color = Color(0xFF0277BD))
            }
        }
    }
}

@Composable
fun SettingItemSwitch(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    value: String = "",
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Color(0xFF37474F))
                Spacer(Modifier.width(12.dp))

                Column {
                    Text(title, fontSize = 16.sp)

                    if (value.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(value, fontSize = 13.sp, color = Color(0xFF0277BD))
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
