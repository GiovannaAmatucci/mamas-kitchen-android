package com.giovanna.amatucci.foodbook.presentation.components.image


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.common.BrushGradient
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme
import com.giovanna.amatucci.foodbook.util.shimmerEffect

@Composable
fun FadedAsyncImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.fadingBottomEdge(),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        },
        error = {
            Image(
                painter = painterResource(R.drawable.food_no_image),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        })
}

fun Modifier.fadingBottomEdge(): Modifier = composed {
    val alphas = AppTheme.alphas
    val fadeOutBrush = remember(alphas) {
        BrushGradient.imageFadeMask(alphas)
    }
    this
        .graphicsLayer(
            alpha = alphas.scrim, compositingStrategy = CompositingStrategy.Offscreen
        )
        .drawWithContent {
            drawContent()
            drawRect(
                brush = fadeOutBrush, blendMode = BlendMode.DstIn
            )
        }
}