package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.giovanna.amatucci.foodbook.ui.theme.AlphaColor

object BrushGradient {
    fun imageFadeMask(alphas: AlphaColor): Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            alphas.scrimLight to Color.Black, alphas.opaque to Color.Transparent
        )
    )

    fun verticalScrim(color: Color, alphas: AlphaColor): Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color.Transparent,
            0.5f to color.copy(alpha = alphas.scrim),
            1.0f to color.copy(alpha = alphas.high)
        )
    )

}
