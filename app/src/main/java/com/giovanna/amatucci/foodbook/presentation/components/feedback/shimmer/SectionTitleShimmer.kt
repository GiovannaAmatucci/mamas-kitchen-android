package com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun SectionTitleShimmer() {
    Box(
        modifier = Modifier
            .width(AppTheme.dimens.recipeCardHeight)
            .height(AppTheme.dimens.paddingExtraLarge)
            .padding(
                start = AppTheme.dimens.paddingMedium,
                bottom = AppTheme.dimens.paddingSmall
            )
            .clip(MaterialTheme.shapes.small)
            .shimmerEffect()
    )
}