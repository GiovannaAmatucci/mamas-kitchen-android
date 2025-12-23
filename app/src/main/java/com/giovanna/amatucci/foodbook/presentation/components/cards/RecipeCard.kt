package com.giovanna.amatucci.foodbook.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.image.FadedAsyncImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeCard(
    recipe: RecipeItem, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    recipe.apply {
        AnimatedCard(
            onClick = onClick, modifier = modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                FadedAsyncImage(
                    imageUrl = imageUrl,
                    modifier = modifier
                        .width(AppTheme.dimens.recipeCardHeight)
                        .fillMaxHeight()
                        .clip(
                            RoundedCornerShape(
                                topStart = AppTheme.dimens.cardCornerRadius,
                                bottomStart = AppTheme.dimens.cardCornerRadius
                            )
                        )
                )
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    name?.let { name ->
                        SectionTitle(
                            title = name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = AppTheme.dimens.maxLinesMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(
                                top = AppTheme.dimens.paddingMedium,
                                start = AppTheme.dimens.paddingMedium
                            )
                        )
                    }
                    description?.let { desc ->
                        SectionSubTitle(
                            subTitle = desc,
                            maxLines = AppTheme.dimens.maxLinesMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(
                                all = AppTheme.dimens.paddingMedium
                            )
                        )
                    }
                }
            }
        }
    }
}