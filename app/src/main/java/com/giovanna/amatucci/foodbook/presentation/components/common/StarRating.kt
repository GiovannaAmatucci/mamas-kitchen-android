package com.giovanna.amatucci.foodbook.presentation.components.common

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.ui.theme.RatingStarColor

@Composable
fun StarRating(
    rating: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = AppTheme.dimens.maxRatingStars,
    activeColor: Color = RatingStarColor,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = AppTheme.alphas.high)
) {
    val ratingContentDescription = stringResource(R.string.cd_rating_description, rating, maxStars)
    val ratingLabel = stringResource(R.string.label_rating_counter, rating, maxStars)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = ratingContentDescription
        }
    ) {
        repeat(maxStars) { index ->
            StarIcon(
                isFilled = index < rating, activeColor = activeColor, inactiveColor = inactiveColor
            )
        }
        Spacer(modifier = Modifier.width(AppTheme.dimens.paddingSmall))
        Text(
            text = ratingLabel,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StarIcon(
    isFilled: Boolean, activeColor: Color, inactiveColor: Color
) {
    val icon = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarBorder
    val tint = if (isFilled) activeColor else inactiveColor

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
    )
}