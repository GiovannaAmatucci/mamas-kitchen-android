package com.giovanna.amatucci.foodbook.presentation.components.recipe

import UiConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.StarRating
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeHeader(
    recipe: RecipeDetails, onCategoryClick: (String) -> Unit
) {
    recipe.apply {
        Column(modifier = Modifier.padding(AppTheme.dimens.paddingMedium)) {
            name?.let { name ->
                SectionTitle(
                    title = name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = UiConstants.Layout.MAX_LINES_LARGE
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            rating?.let { rating ->
                StarRating(
                    rating = rating,
                    modifier = Modifier.padding(bottom = AppTheme.dimens.paddingSmall)
                )
            }
            description?.let { description ->
                SectionSubTitle(
                    subTitle = description,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = UiConstants.Layout.MAX_LINES_LARGE
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
            }
            categories?.let { categories ->
                SubCategoriesChips(
                    categories = categories,
                    modifier = Modifier.padding(vertical = AppTheme.dimens.paddingSmall),
                    onTagClick = onCategoryClick
                )
            }
        }
    }
}