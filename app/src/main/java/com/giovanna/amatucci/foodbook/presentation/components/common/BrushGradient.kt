package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.giovanna.amatucci.foodbook.ui.theme.AlphaColor

object BrushGradient {
    fun verticalScrim(
        color: Color, startAlpha: Float, endAlpha: Float, alphas: AlphaColor
    ): Brush {
        fun getAlpha(factor: Float): Float {
            return startAlpha + (endAlpha - startAlpha) * factor
        }

        return Brush.verticalGradient(
            0.0f to color.copy(alpha = startAlpha),
            0.15f to color.copy(alpha = getAlpha(alphas.easeStep1)),
            0.30f to color.copy(alpha = getAlpha(alphas.easeStep2)),
            0.45f to color.copy(alpha = getAlpha(alphas.easeStep3)),
            0.60f to color.copy(alpha = getAlpha(alphas.easeStep4)),
            0.75f to color.copy(alpha = getAlpha(alphas.easeStep5)),
            0.90f to color.copy(alpha = getAlpha(alphas.easeStep6)),
            1.0f to color.copy(alpha = endAlpha)
        )
    }
}


