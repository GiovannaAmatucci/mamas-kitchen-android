package com.giovanna.amatucci.foodbook.presentation.search.content

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer.CategorySectionShimmer
import com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer.HeroCardShimmer
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun SearchScreenShimmer() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimens.paddingMedium),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(AppTheme.dimens.paddingExtraSmall))
            repeat(5) { CategorySectionShimmer() }
        }
        Box(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimens.paddingMedium,
                    vertical = AppTheme.dimens.paddingSmall
                )
                .width(AppTheme.dimens.imageSizeMedium)
                .height(AppTheme.dimens.paddingLarge)
                .clip(MaterialTheme.shapes.small)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
        Column(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium)
        ) {
            repeat(3) { HeroCardShimmer() }
        }
    }
}



