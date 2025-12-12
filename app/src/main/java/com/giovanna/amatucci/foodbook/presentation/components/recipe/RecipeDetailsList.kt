package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.image.ImageDetails
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeDetailsList(
    recipe: RecipeDetails,
    modifier: Modifier,
    onImageDisplayed: (String?) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    recipe.apply {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
        ) {
            imageUrls?.let { imageUrls ->
                item {
                    ImageDetails(
                        images = imageUrls, onImageDisplayed = onImageDisplayed
                    )
                }
            }
            item {
                RecipeHeader(recipe = recipe, onCategoryClick = onCategoryClick)
                Spacer(modifier = Modifier.padding(AppTheme.dimens.paddingSmall))
            }

            item {
                RecipeStatsRow(recipe = recipe)
                Spacer(modifier = Modifier.padding(AppTheme.dimens.paddingSmall))
            }

            item {
                SectionTitle(
                    stringResource(R.string.details_section_title_ingredients),
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.paddingMedium)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            }
            itemsIndexed(
                items = ingredients,
                key = { index, ingredient -> "${ingredient.description}_$index" }) { _, ingredient ->
                RecipeIngredientItem(ingredient = ingredient)
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            }

            item {
                SectionTitle(
                    stringResource(R.string.details_section_title_instructions),
                    modifier = Modifier.padding(horizontal = AppTheme.dimens.paddingMedium)
                )
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            }
            items(items = directions) { directions ->
                RecipeInstructionItem(instruction = directions, modifier = Modifier)
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))
            }
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingLarge))
            }
        }
    }
}