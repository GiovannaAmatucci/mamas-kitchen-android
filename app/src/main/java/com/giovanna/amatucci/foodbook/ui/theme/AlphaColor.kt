package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class AlphaColor(
    val opaque: Float = 1.0f,
    val high: Float = 0.9f,
    val medium: Float = 0.7f,
    val disabled: Float = 0.38f,
    val scrim: Float = 0.8f,
    val scrimLight: Float = 0.5f,
    val faint: Float = 0.12f
)

val LocalAlphaColor = staticCompositionLocalOf { AlphaColor() }