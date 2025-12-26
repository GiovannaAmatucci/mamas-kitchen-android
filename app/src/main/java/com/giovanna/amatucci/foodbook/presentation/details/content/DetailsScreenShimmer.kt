package com.giovanna.amatucci.foodbook.presentation.details.content

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer.SectionTitleShimmer
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun DetailsScreenShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.imageSizeLarge)
                .shimmerEffect()
        )
        Column(modifier = Modifier.padding(AppTheme.dimens.paddingMedium)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(AppTheme.alphas.medium)
                    .height(AppTheme.dimens.paddingExtraLarge)
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimens.paddingMedium)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth(AppTheme.alphas.medium)
                    .height(AppTheme.dimens.paddingMedium)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.paddingSmall,
                    horizontal = AppTheme.dimens.paddingMedium
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            repeat(3) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(AppTheme.dimens.paddingMedium)
                            .height(AppTheme.dimens.paddingMedium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
                    Box(
                        modifier = Modifier
                            .width(AppTheme.dimens.paddingExtraLarge)
                            .height(AppTheme.dimens.paddingMedium)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
        SectionTitleShimmer()
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimens.paddingExtraLarge)
                    .padding(
                        horizontal = AppTheme.dimens.paddingMedium,
                        vertical = AppTheme.dimens.paddingExtraSmall
                    )
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
        SectionTitleShimmer()
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimens.paddingExtraLarge)
                    .padding(
                        horizontal = AppTheme.dimens.paddingMedium,
                        vertical = AppTheme.dimens.paddingExtraSmall
                    )
                    .clip(MaterialTheme.shapes.small)
                    .shimmerEffect()
            )
        }
    }
}


