package com.nilam.moodtrackerapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

@Composable
fun MoodBackground(
    mood: String?,
    modifier: Modifier = Modifier
) {
    // 🎨 Pilih warna sesuai mood
    val targetColor = when (mood) {
        "😄" -> Color(0xFFB2FF59)
        "😐" -> Color(0xFFFFF59D)
        "😢" -> Color(0xFF81D4FA)
        "😡" -> Color(0xFFFF8A80)
        "😴" -> Color(0xFFD1C4E9)
        else -> Color(0xFFE3F2FD) // Default netral
    }

    // 🌈 Smooth transition antar warna
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(1200, easing = FastOutSlowInEasing)
    )

    // 🌊 Animasi gerak linear gradient
    val infiniteTransition = rememberInfiniteTransition()

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f, // lebih besar biar geraknya kelihatan
        animationSpec = infiniteRepeatable(
            tween(9000, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )

    // ✨ Animasi shimmer halus (efek bernafas)
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            tween(3500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    // 🎨 Layer 1 — gradient utama
    val gradientMain = Brush.linearGradient(
        colors = listOf(
            animatedColor,
            animatedColor.copy(alpha = 0.8f),
            animatedColor.copy(alpha = 0.55f)
        ),
        start = Offset(0f, offset),
        end = Offset(offset, 0f)
    )

    // 🎨 Layer 2 — shimmer lembut
    val gradientShimmer = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = shimmerAlpha * 0.4f),
            Color.Transparent
        ),
        center = Offset(offset / 2, offset / 2),
        radius = 600f
    )

    // 🔥 Combine dua gradient: lebih bertekstur & hidup
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientMain)
            .background(gradientShimmer)
    )
}
