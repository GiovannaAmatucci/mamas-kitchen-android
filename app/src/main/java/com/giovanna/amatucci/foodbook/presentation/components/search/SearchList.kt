package com.giovanna.amatucci.foodbook.presentation.components.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.MessageComponent
import com.giovanna.amatucci.foodbook.presentation.components.feedback.HandlePagingState
import com.giovanna.amatucci.foodbook.presentation.components.feedback.NetworkErrorComponent
import com.giovanna.amatucci.foodbook.presentation.components.recipe.RecipeGridList

@Composable
fun SearchList(
    searchQuery: String,
    recipes: LazyPagingItems<RecipeItem>,
    onRecipeClick: (String) -> Unit,
) {
    HandlePagingState(
        pagingItems = recipes,
        emptyContent = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MessageComponent(stringResource(R.string.search_empty_message, searchQuery))
            }
        },
        errorContent = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NetworkErrorComponent(onRetry = { recipes.retry() })
            }
        }
    ) { loadedRecipes ->
        RecipeGridList(
            recipes = loadedRecipes, onRecipeClick = onRecipeClick
        )
    }
}