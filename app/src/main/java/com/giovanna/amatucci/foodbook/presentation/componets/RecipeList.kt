package com.giovanna.amatucci.foodbook.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
        items(recipes.itemCount) { index ->
            recipes[index]?.let { recipe ->
                RecipeListItem(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }
    }
}