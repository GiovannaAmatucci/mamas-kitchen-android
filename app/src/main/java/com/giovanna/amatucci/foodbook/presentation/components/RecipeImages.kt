package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.ui.theme.Shape

@Composable
fun RecipeImages(images: List<String?>) {
    if (images.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { images.size })
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState, modifier = Modifier
            ) {
                AppImage(
                    model = images[it],
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ImageSizeLarge)
                        .clip(Shape.small)
                )
            }

            if (pagerState.pageCount > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                PagerIndicator(
                    pageCount = pagerState.pageCount, currentPage = pagerState.currentPage
                )
            }
        }
    } else {
        NoImagePlaceholder(
            modifier = Modifier
        )
    }
}