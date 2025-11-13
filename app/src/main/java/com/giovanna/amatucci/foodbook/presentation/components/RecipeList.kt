package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem

@Composable
fun RecipeList(recipes: LazyPagingItems<RecipeItem>, onRecipeClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = recipes.itemCount,
            key = { index ->
                recipes[index]?.id ?: recipes[index]?.name ?: index
            }
        ) { index ->
            // ===========================================

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
