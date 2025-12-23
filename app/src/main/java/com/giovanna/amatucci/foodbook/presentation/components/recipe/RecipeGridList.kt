package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.cards.RecipeCard
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeGridList(
    recipes: LazyPagingItems<RecipeItem>, onRecipeClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(AppTheme.dimens.listGridCellsFixed),
        contentPadding = PaddingValues(AppTheme.dimens.paddingSmall),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall)
    ) {
        items(
            count = recipes.itemCount, key = recipes.itemKey { recipe ->
                recipe.id ?: recipe.hashCode()
            }
        ) { index ->
            val recipe = recipes[index]
            if (recipe != null) {
                RecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id.toString()) }
                )
            }
        }
    }
}