package com.giovanna.amatucci.foodbook.presentation.components

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
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.ui.theme.Shape
import com.giovanna.amatucci.foodbook.util.shimmerEffect

/**
 * A placeholder card that displays a loading shimmer effect.
 * Mirrors the structure of [RecipeCard] (Horizontal Layout) using Theme Dimens.
 */
@Composable
fun RecipeCardShimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.RecipeCardHeight)
            .padding(vertical = Dimens.PaddingVerticalCard, horizontal = Dimens.PaddingSmall),
        shape = RoundedCornerShape(Dimens.PaddingMedium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(Dimens.RecipeCardHeight)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = Dimens.PaddingMedium, bottomStart = Dimens.PaddingMedium
                        )
                    )
                    .shimmerEffect()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingMedium),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(Dimens.ShimmerTitleWidthFraction)
                        .height(Dimens.ShimmerTitleHeight)
                        .clip(Shape.extraSmall)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ShimmerTextHeight)
                        .clip(Shape.extraSmall)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(Dimens.ShimmerTextWidthFraction)
                        .height(Dimens.ShimmerTextHeight)
                        .clip(Shape.extraSmall)
                        .shimmerEffect()
                )
            }
        }
    }
}