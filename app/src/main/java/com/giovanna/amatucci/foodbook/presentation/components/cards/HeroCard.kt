package com.giovanna.amatucci.foodbook.presentation.components.cards

import UiConstants
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.GradientScrim
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.image.FadedAsyncImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun HeroCard(
    recipe: RecipeItem, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    recipe.apply {
        AnimatedCard(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.heroCardHeight)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                FadedAsyncImage(
                    imageUrl = imageUrl, modifier = Modifier.fillMaxSize()
                )
                GradientScrim()
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(AppTheme.dimens.paddingMedium)
                ) {
                    name?.let { name ->
                        SectionTitle(
                            title = name.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            maxLines = UiConstants.Layout.MAX_LINES_LARGE,
                            modifier = Modifier
                        )
                    }
                    description.let { description ->
                        if (!description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
                            SectionSubTitle(
                                subTitle = description,
                                color = Color.White.copy(
                                    alpha = AppTheme.alphas.medium
                                ),
                                modifier = modifier.padding(
                                    bottom = AppTheme.dimens.paddingMedium
                                ),
                                textAlign = TextAlign.Start,
                                maxLines = UiConstants.Layout.MAX_LINES_MEDIUM
                            )
                        }
                    }
                }
            }
        }
    }
}