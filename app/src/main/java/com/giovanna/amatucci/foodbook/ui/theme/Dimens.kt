package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val paddingExtraSmall: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val paddingExtraLarge: Dp = 32.dp,

    val iconSizeSmall: Dp = 20.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 40.dp,

    val imageSizeMedium: Dp = 150.dp,
    val imageSizeLarge: Dp = 200.dp,
    val imageSizeExtraLarge: Dp = 300.dp,

    val cardCornerRadius: Dp = 16.dp,
    val cardElevation: Dp = 2.dp,
    val cardElevationPressed: Dp = 1.dp,
    val cardPressedScale: Float = 0.96f,

    val categoryCardHeight: Dp = 90.dp,
    val categoryCardExpandedWidth: Dp = 200.dp,
    val categoryIconSize: Dp = 60.dp,
    val recipeCardHeight: Dp = 140.dp,
    val heroCardHeight: Dp = 200.dp,

    val blurRadius: Dp = 30.dp, val pagerIndicatorSpacing: Dp = 8.dp, val pageCount: Int = 1,

    val maxLinesDefault: Int = 1, val maxLinesMedium: Int = 2, val maxLinesLarge: Int = 3,

    val weightDefault: Float = 1f,

    val listGridCellsFixed: Int = 1,

    val animationDelay: Long = 2000L,
    val animationDuration: Int = 1500,
    val maxRatingStars: Int = 5
)

val LocalDimens = staticCompositionLocalOf { Dimensions() }