package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.image.FadedAsyncImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun BlurredBackground(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = AppTheme.dimens.blurRadius,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (imageUrl != null) {
            FadedAsyncImage(
                imageUrl = imageUrl, modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.food_no_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius),
                contentScale = ContentScale.Crop
            )
        }
        content()
    }
}