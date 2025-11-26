package com.nilam.moodtrackerapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary =  androidx.compose.ui.graphics.Color(0xFF0277BD),
    secondary = androidx.compose.ui.graphics.Color(0xFF0288D1),
    background = androidx.compose.ui.graphics.Color(0xFFE3F2FD)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF81D4FA),
    secondary = androidx.compose.ui.graphics.Color(0xFF4FC3F7),
    background = androidx.compose.ui.graphics.Color(0xFF121212)
)

@Composable
fun MoodTrackerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}

