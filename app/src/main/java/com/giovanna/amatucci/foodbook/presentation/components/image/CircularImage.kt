package com.giovanna.amatucci.foodbook.presentation.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun CircularImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    containerSize: Dp = AppTheme.dimens.imageSizeLarge,
    imageWidth: Dp = AppTheme.dimens.imageSizeLarge,
    imageHeight: Dp = AppTheme.dimens.imageSizeExtraLarge
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(containerSize)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = AppTheme.alphas.disabled),
                shape = CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(imageHeight)
                .width(imageWidth)
        )
    }
}