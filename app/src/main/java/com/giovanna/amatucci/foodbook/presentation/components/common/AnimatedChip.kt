package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.domain.model.Category
import com.giovanna.amatucci.foodbook.presentation.components.image.IconImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun AnimatedChip(
    category: Category, isSelected: Boolean, onClick: () -> Unit
) {
    with(AppTheme.dimens) {
        val targetWidth = if (isSelected) categoryCardExpandedWidth else categoryCardHeight
        val targetColor =
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        val contentColor =
            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        val elevation = if (isSelected) cardElevation else cardElevationPressed
        val width by animateDpAsState(
            targetValue = targetWidth, animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
        val backgroundColor by animateColorAsState(
            targetValue = targetColor
        )

        Surface(
            modifier = Modifier
                .height(categoryCardHeight)
                .width(width)
                .noRippleClickable(onClick),
            shape = CircleShape,
            color = backgroundColor,
            shadowElevation = elevation
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconImage(
                    imageRes = category.imageRes,
                    iconSize = categoryIconSize,
                )

                ChipText(
                    isVisible = isSelected, textRes = category.title, textColor = contentColor
                )
            }
        }
    }
}

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick
)
