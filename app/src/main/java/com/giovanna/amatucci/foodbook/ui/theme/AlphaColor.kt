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
    val scrimLight: Float = 0.6f,
    val scrimSoft: Float = 0.2f,
    val faint: Float = 0.12f,

    val easeStep1: Float = 0.05f,
    val easeStep2: Float = 0.15f,
    val easeStep3: Float = 0.30f,
    val easeStep4: Float = 0.50f,
    val easeStep5: Float = 0.75f,
    val easeStep6: Float = 0.90f
)

val LocalAlphaColor = staticCompositionLocalOf { AlphaColor() }