package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import com.giovanna.amatucci.foodbook.ui.theme.BrushGradient.verticalScrim

fun Modifier.fadedBottomEdge(
    startAlpha: Float? = null,
    endAlpha: Float = 0f
): Modifier = composed {
    val alphas = LocalAlphaColor.current
    val effectiveStartAlpha = startAlpha ?: alphas.opaque
    this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithCache {
            val brush = verticalScrim(
                color = Color.Black,
                startAlpha = effectiveStartAlpha,
                endAlpha = endAlpha,
                alphas = alphas
            )
            onDrawWithContent {
                drawContent()
                drawRect(brush = brush, blendMode = BlendMode.DstIn)
            }
        }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
