package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.AppBarHeight
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.CardElevation
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.IconSizeMedium
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.ImageSizeLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingExtraLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingExtraSmall
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingMedium
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingSmall
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.ScreenPadding


/**
 * ### Common Spacing and Sizes
 *
 * @param PaddingExtraSmall Very small spacing or size (e.g., for minimalist layout elements or fine borders).
 * @param PaddingSmall Small spacing or size (e.g., between nearby elements, icons, and text).
 * @param PaddingMedium Medium spacing or size, a general-purpose value for most layouts (e.g., between sections, card padding).
 * @param PaddingLarge Large spacing or size, to create clear separation between main sections or screen padding.
 * @param PaddingExtraLarge Very large spacing or size, to create significant visual impact or wide margins.
 *
 * @param ScreenPadding The default padding applied to the edges of most screens.
 * @param CardElevation Default elevation for cards and surfaces that float slightly above the background.
 * @param IconSizeMedium Default medium size for icons.
 * @param ImageSizeLarge Default large size for main or featured images.
 * @param AppBarHeight Default height for the top bar (TopAppBar).
 */
object Dimens {
    val PaddingExtraSmall = 4.dp
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp
    val PaddingExtraLarge = 32.dp

    val ScreenPadding = 16.dp
    val CardElevation = 2.dp

    val IconSizeMedium = 24.dp
    val ImageSizeLarge = 200.dp

    val ImageSizeSmall = 150.dp

    val RecipeCardHeight = 250.dp


    val AppBarHeight = 56.dp

    val BlurRadius = 30.dp

    val PagerIndicatorSpacing = 12.dp
}