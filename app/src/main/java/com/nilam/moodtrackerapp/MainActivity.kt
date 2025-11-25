package com.nilam.moodtrackerapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import com.nilam.moodtrackerapp.MoodEntry
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import com.nilam.moodtrackerapp.ui.screens.AddMoodScreen
import com.nilam.moodtrackerapp.ui.screens.HomeScreen
import com.nilam.moodtrackerapp.ui.screens.MoodBoardScreen
import com.nilam.moodtrackerapp.ui.screens.QuoteScreen
import com.nilam.moodtrackerapp.ui.screens.SettingScreen
import com.nilam.moodtrackerapp.utils.NotificationUtils
import com.nilam.moodtrackerapp.utils.LocaleHelper
import com.nilam.moodtrackerapp.viewmodel.MoodViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }


        NotificationUtils.createNotificationChannel(this)

        setContent {
            MainApp()
        }
    }
}



    @Composable
fun MainApp() {

    var selectedPage by remember { mutableStateOf(0) }
    var showAddScreen by remember { mutableStateOf(false) }
    val vm: MoodViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    Scaffold(
        bottomBar = { HomeBottomBar(selectedPage) { selectedPage = it } }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // =========== SCREEN ADD MOOD ============
            if (showAddScreen) {
                AddMoodScreen(
                    onSave = { moodEntry ->
                        vm.addMood(moodEntry)
                        showAddScreen = false
                    },
                    onBack = { showAddScreen = false }
                )
            }

            // =========== NAVIGASI PAGE BAWAH ============
            else {
                when (selectedPage) {
                    0 -> HomeScreen(
                        moodViewModel = vm,
                        onAdd = { showAddScreen = true }
                    )


                    1 -> QuoteScreen()

                    2 -> MoodBoardScreen(moodViewModel = vm)

                    3 -> SettingScreen()   // <<< PENTING — masuk ke SettingScreen asli
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(selectedPage: Int, onPageSelected: (Int) -> Unit) {
    NavigationBar(containerColor = Color(0xBAFFFFFF)) {

        NavigationBarItem(
            selected = selectedPage == 0,
            onClick = { onPageSelected(0) },
            icon = {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = stringResource(R.string.nav_diary_desc)
                )
            },
            label = { Text(stringResource(R.string.nav_diary)) }
        )

        NavigationBarItem(
            selected = selectedPage == 1,
            onClick = { onPageSelected(1) },
            icon = {
                Icon(
                    Icons.Default.Category,
                    contentDescription = stringResource(R.string.nav_quotes_desc)
                )
            },
            label = { Text(stringResource(R.string.nav_quotes)) }
        )

        Spacer(Modifier.weight(1f))

        NavigationBarItem(
            selected = selectedPage == 2,
            onClick = { onPageSelected(2) },
            icon = {
                Icon(
                    Icons.Default.Dashboard,
                    contentDescription = stringResource(R.string.nav_moodboard_desc)
                )
            },
            label = { Text(stringResource(R.string.nav_moodboard)) }
        )

        NavigationBarItem(
            selected = selectedPage == 3,
            onClick = { onPageSelected(3) },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.nav_setting_desc)
                )
            },
            label = { Text(stringResource(R.string.nav_setting)) }
        )
    }
}
