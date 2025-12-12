package com.giovanna.amatucci.foodbook.presentation.components.common

import UiConstants
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.ui.theme.RatingStarColor

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = UiConstants.Components.Rating.MAX_STARS
) {
    val ratingDescription = "$rating out of $maxStars rating"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = ratingDescription
        }) {
        repeat(maxStars) { index ->
            val isFilled = index < rating

            val icon = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarBorder
            val tint = if (isFilled) RatingStarColor
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = AppTheme.alphas.high)

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
            )
        }

        Spacer(modifier = Modifier.width(AppTheme.dimens.paddingSmall))
        Text(
            text = "$rating/$maxStars",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}