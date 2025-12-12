package com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun HeroCardShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.heroCardHeight)
            .clip(MaterialTheme.shapes.large)
            .shimmerEffect()
    )
}