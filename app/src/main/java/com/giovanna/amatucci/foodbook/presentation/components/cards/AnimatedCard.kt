package com.giovanna.amatucci.foodbook.presentation.components.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedCard(
    onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) AppTheme.dimens.cardPressedScale else AppTheme.alphas.opaque
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.recipeCardHeight)
            .padding(
                vertical = AppTheme.dimens.paddingSmall, horizontal = AppTheme.dimens.paddingSmall
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(AppTheme.dimens.cardCornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.dimens.cardElevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}