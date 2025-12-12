package com.giovanna.amatucci.foodbook.presentation.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun IconImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    iconSize: Dp = AppTheme.dimens.categoryIconSize
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(iconSize)
    )
}