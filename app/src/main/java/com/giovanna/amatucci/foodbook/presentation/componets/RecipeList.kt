package com.giovanna.amatucci.foodbook.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary

@Composable
fun RecipeList(recipes: List<RecipeSummary>, onRecipeClick: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(recipes, key = { it.id }) { recipe ->
            RecipeListItem(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
        }
    }
}