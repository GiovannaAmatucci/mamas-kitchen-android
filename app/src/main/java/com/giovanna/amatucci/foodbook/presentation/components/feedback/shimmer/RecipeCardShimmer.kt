package com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun RecipeCardShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.recipeCardHeight)
            .padding(
                vertical = AppTheme.dimens.paddingSmall,
                horizontal = AppTheme.dimens.paddingSmall
            ),
        shape = RoundedCornerShape(AppTheme.dimens.cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(AppTheme.dimens.recipeCardHeight)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = AppTheme.dimens.cardCornerRadius,
                            bottomStart = AppTheme.dimens.cardCornerRadius
                        )
                    )
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(AppTheme.dimens.iconSizeSmall)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.dimens.paddingSmall)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(AppTheme.alphas.medium)
                        .height(AppTheme.dimens.paddingSmall)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .shimmerEffect()
                )
            }
        }
    }
}