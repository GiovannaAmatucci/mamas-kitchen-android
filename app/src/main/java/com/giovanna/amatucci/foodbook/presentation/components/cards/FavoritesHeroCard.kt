package com.giovanna.amatucci.foodbook.presentation.components.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.ui.theme.fadedBottomEdge
import com.giovanna.amatucci.foodbook.presentation.components.image.FadedAsyncImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun FavoritesHeroCard(
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
                    imageUrl = imageUrl, modifier = Modifier
                        .fillMaxSize()
                        .fadedBottomEdge()
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(AppTheme.dimens.paddingMedium)
                ) {
                    name?.let { name ->
                        SectionTitle(
                            title = name.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            maxLines = AppTheme.dimens.maxLinesDefault,
                            modifier = Modifier
                        )
                    }
                    description.let { description ->
                        if (!description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
                            SectionSubTitle(
                                subTitle = description,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = AppTheme.alphas.scrimLight),
                                modifier = modifier.padding(
                                    bottom = AppTheme.dimens.paddingMedium
                                ),
                                textAlign = TextAlign.Start,
                                maxLines = AppTheme.dimens.maxLinesMedium
                            )
                        }
                    }
                }
            }
        }
    }
}