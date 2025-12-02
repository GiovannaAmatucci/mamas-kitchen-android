package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.ui.theme.Shape
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun RecipeDetailsShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ImageSizeLarge)
                .shimmerEffect()
        )
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(Dimens.ShimmerTextWidthFraction)
                    .height(Dimens.PaddingExtraLarge)
                    .clip(Shape.small)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ShimmerTextHeight)
                    .clip(Shape.extraSmall)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth(Dimens.ShimmerTextWidthFraction)
                    .height(Dimens.ShimmerTextHeight)
                    .clip(Shape.extraSmall)
                    .shimmerEffect()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingSmall, horizontal = Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            repeat(3) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(Dimens.PaddingMedium)
                            .height(Dimens.PaddingMedium)
                            .clip(Shape.extraSmall)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
                    Box(
                        modifier = Modifier
                            .width(Dimens.PaddingExtraLarge)
                            .height(Dimens.PaddingMedium)
                            .clip(Shape.extraSmall)
                            .shimmerEffect()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        SectionTitleShimmer()
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.PaddingExtraLarge)
                    .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingExtraSmall)
                    .clip(Shape.extraSmall)
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        SectionTitleShimmer()
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.PaddingExtraLarge)
                    .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingExtraSmall)
                    .clip(Shape.small)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
private fun SectionTitleShimmer() {
    Box(
        modifier = Modifier
            .width(Dimens.RecipeCardHeight)
            .height(Dimens.PaddingExtraLarge)
            .padding(start = Dimens.PaddingMedium, bottom = Dimens.PaddingSmall)
            .clip(Shape.small)
            .shimmerEffect()
    )
}