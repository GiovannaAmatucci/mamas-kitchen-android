package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.util.constants.UiConstants
import com.giovanna.amatucci.foodbook.util.shimmerEffect

/**
 * A component that displays a carousel of images for the recipe details.
 * It applies a vertical fade-out gradient at the bottom of the image.
 *
 * @param images A list of image URLs to display.
 * @param onImageDisplayed Callback invoked when an image becomes the current page.
 *                         Returns null if there are no images.
 */
@Composable
fun DetailsImageComposable(
    images: List<String>, onImageDisplayed: (String?) -> Unit
) {
    val fadeOutBrush = remember {
        Brush.verticalGradient(
            UiConstants.RECIPE_DETAILS_IMAGE_GRADIENT_FADE_START_PERCENT to Color.Black,
            UiConstants.RECIPE_DETAILS_IMAGE_GRADIENT_COLOR_TRANSPARENT to Color.Transparent
        )
    }

    val fadeOutModifier = remember(fadeOutBrush) {
        Modifier
            .graphicsLayer(
                alpha = UiConstants.RECIPE_DETAILS_IMAGE_GRAPHICS_LAYER_ALPHA_HACK,
                compositingStrategy = CompositingStrategy.Offscreen
            )
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = fadeOutBrush,
                    blendMode = BlendMode.DstIn
                )
            }
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
                SubcomposeAsyncImage(
                    model = images[pageIndex],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ImageSizeLarge)
                        .then(fadeOutModifier),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    },
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.food_no_image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                )
            }

            if (pagerState.pageCount > UiConstants.RECIPE_DETAILS_PAGE_COUNT_MIN) {
                Spacer(modifier = Modifier.height(Dimens.PagerIndicatorSpacing))
                PagerIndicatorComposable(
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