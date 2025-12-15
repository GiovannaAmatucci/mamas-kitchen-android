package com.giovanna.amatucci.foodbook.presentation.components.image

import UiConstants
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.common.PagerIndicator
import com.giovanna.amatucci.foodbook.presentation.components.common.fadedBottomEdge
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun ImageDetails(
    images: List<String>, onImageDisplayed: (String?) -> Unit, modifier: Modifier = Modifier
) {
    val imageHeight = AppTheme.dimens.imageSizeLarge
    val spaceHeight = AppTheme.dimens.pagerIndicatorSpacing
    val pageCount = images.size
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val currentPage = pagerState.currentPage

    if (images.isNotEmpty()) {
        LaunchedEffect(currentPage) {
            onImageDisplayed(images[currentPage])
        }
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState) { pageIndex ->
                FadedAsyncImage(
                    imageUrl = images[pageIndex],
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .fadedBottomEdge()
                )
            }
            if (pageCount > UiConstants.Details.PAGE_COUNT_MIN) {
                Spacer(
                    modifier = Modifier.height(
                        spaceHeight
                    )
                )
                PagerIndicator(pageCount = pageCount, currentPage = currentPage)
            }
        }
    } else {
        NoImageDetails(
            imageHeight = imageHeight, modifier = modifier
        )
        LaunchedEffect(Unit) {
            onImageDisplayed(null)
        }
    }
}


@Composable
private fun NoImageDetails(imageHeight: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.food_no_image),
        contentDescription = stringResource(R.string.details_no_images),
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
            .fadedBottomEdge(),
        contentScale = ContentScale.Crop
    )
}