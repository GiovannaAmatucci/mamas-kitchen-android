package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.util.constants.UiConstants

/**
 * A grid list that displays recipe cards.
 * Uses paging to load items lazily.
 *
 * @param recipes The paginated list of recipes.
 * @param onRecipeClick Callback triggered when a recipe card is clicked, passing the recipe ID.
 */
@Composable
fun RecipeList(recipes: LazyPagingItems<RecipeItem>, onRecipeClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(UiConstants.RECIPE_LIST_GRID_CELLS_FIXED),
        contentPadding = PaddingValues(Dimens.PaddingSmall),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
    ) {
        items(
            count = recipes.itemCount,
            key = { index ->
                recipes[index]?.id ?: recipes[index]?.name ?: index
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
