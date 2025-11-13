package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.di.util.constants.UiConstants
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

@Composable
fun RecipeDetailsImage(
    images: List<String>, onImageDisplayed: (String?) -> Unit
) {
    val fadeOutBrush = Brush.verticalGradient(
        UiConstants.GRADIENT_FADE_START_PERCENT to Color.Black,
        UiConstants.GRADIENT_COLOR_TRANSPARENT to Color.Transparent
    )
    val fadeOutModifier =
        Modifier
            .graphicsLayer(alpha = UiConstants.GRAPHICS_LAYER_ALPHA_HACK)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = fadeOutBrush, blendMode = BlendMode.DstIn
                )
            }
    if (images.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { images.size })
        LaunchedEffect(pagerState.currentPage) {
            onImageDisplayed(images[pagerState.currentPage])
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState, modifier = Modifier
            ) { pageIndex ->
                AsyncImage(
                    model = images[pageIndex],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ImageSizeLarge)
                        .then(fadeOutModifier)
                )
            }

            if (pagerState.pageCount > 1) {
                Spacer(modifier = Modifier.height(Dimens.PagerIndicatorSpacing))
                PagerIndicator(
                    pageCount = pagerState.pageCount, currentPage = pagerState.currentPage
                )
            }
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.food_no_image),
            contentDescription = stringResource(R.string.details_no_images),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ImageSizeLarge)
                .then(fadeOutModifier),
            contentScale = ContentScale.Crop
        )

        LaunchedEffect(Unit) {
            onImageDisplayed(null)
        }
    }
}