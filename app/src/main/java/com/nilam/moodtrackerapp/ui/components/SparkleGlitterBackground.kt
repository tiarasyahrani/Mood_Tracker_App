package com.nilam.moodtrackerapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun SparkleGlitterBackground(
    mood: String?,
    modifier: Modifier = Modifier
) {
    val baseColor = when (mood) {
        "😄" -> Color(0xFFB2FF59)
        "😐" -> Color(0xFFFFF59D)
        "😢" -> Color(0xFF81D4FA)
        "😡" -> Color(0xFFFF8A80)
        "😴" -> Color(0xFFD1C4E9)
        else -> Color(0xFFE3F2FD)
    }

    val glitterColors = listOf(
        Color(0xFFFF80AB),  // pink sparkly
        Color(0xFF82B1FF),  // blue sparkle
        Color(0xFFB388FF),  // purple glitter
        Color(0xFFFFF176),  // gold yellow
        Color(0xFFEA80FC),  // neon purple
        Color(0xFF80DEEA),  // cyan
        Color(0xFFFFCC80)   // orange pastel
    )

    // Model sparkle
    data class Sparkle(
        val x: Float,
        val y: Float,
        val size: Float,
        val color: Color
    )

    // Generate sparkle random + warna random
    val sparkles = remember {
        List(40) {
            Sparkle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 10f + 4f,
                color = glitterColors.random()
            )
        }
    }

    val infinite = rememberInfiniteTransition()

    // scale animation (membesar-mengecil)
    val scaleAnim = infinite.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            tween(1500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    // opacity animation
    val alphaAnim = infinite.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {

        // Background gradient lembut
        drawRect(
            brush = Brush.linearGradient(
                listOf(
                    baseColor,
                    baseColor.copy(alpha = 0.7f),
                    baseColor.copy(alpha = 0.5f)
                )
            )
        )

        sparkles.forEach { spk ->
            val px = spk.x * size.width
            val py = spk.y * size.height

            drawCircle(
                color = spk.color.copy(alpha = alphaAnim.value),
                radius = spk.size * scaleAnim.value,
                center = Offset(px, py)
            )
        }
    }
}
