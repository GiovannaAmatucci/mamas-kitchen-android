package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.util.constants.UiConstants

@Composable
fun PagerIndicatorComposable(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(UiConstants.PAGER_INDICATOR_SPACE.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(
                alpha = UiConstants.PAGER_INDICATOR_SURFACE_ALPHA
            )
            Box(
                modifier = Modifier
                    .size(Dimens.PaddingSmall)
                    .clip(MaterialTheme.shapes.small)
                    .background(color)
            )
        }
    }
}