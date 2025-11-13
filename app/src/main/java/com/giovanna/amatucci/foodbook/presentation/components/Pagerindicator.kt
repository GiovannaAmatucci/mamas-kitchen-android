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

@Composable
fun PagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.2f
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