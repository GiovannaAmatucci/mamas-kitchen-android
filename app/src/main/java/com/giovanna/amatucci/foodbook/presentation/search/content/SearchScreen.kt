package com.giovanna.amatucci.foodbook.presentation.search.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.components.common.MessageComponent
import com.giovanna.amatucci.foodbook.presentation.components.common.RecentFavoritesSection
import com.giovanna.amatucci.foodbook.presentation.components.feedback.FeedbackComponent
import com.giovanna.amatucci.foodbook.presentation.components.feedback.HandlePagingState
import com.giovanna.amatucci.foodbook.presentation.components.feedback.NetworkErrorComponent
import com.giovanna.amatucci.foodbook.presentation.components.recipe.CategorySection
import com.giovanna.amatucci.foodbook.presentation.components.recipe.RecipeGridList
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateToRecipe: (id: String) -> Unit, state: SearchUiState, onEvent: (SearchEvent) -> Unit
) {
    state.apply {
        if (status == ScreenStatus.Loading) SearchScreenShimmer()
        else Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
        ) {
            CategorySection(
                categories = categories, currentQuery = submittedQuery, onCategoryClick = { query ->
                    if (submittedQuery != query) {
                        onEvent(SearchEvent.UpdateSearchQuery(query))
                        onEvent(SearchEvent.SubmitSearch(query))
                    } else {
                        onEvent(SearchEvent.UpdateSearchQuery(""))
                        onEvent(SearchEvent.SubmitSearch(""))
                    }
                })
            SearchContent(
                searchQuery = submittedQuery,
                recipes = recipes.collectAsLazyPagingItems(),
                onRecipeClick = onNavigateToRecipe,
                recentFavorites = lastFavorites
            )
        }
    }
}

@Composable
private fun SearchContent(
    searchQuery: String,
    recipes: LazyPagingItems<RecipeItem>,
    recentFavorites: List<RecipeItem>?,
    onRecipeClick: (String) -> Unit
) {
    if (searchQuery.isBlank()) {
        if (recentFavorites?.isNotEmpty() == true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                RecentFavoritesSection(
                    recipes = recentFavorites, onRecipeClick = onRecipeClick, modifier = Modifier
                )
            }
        } else if (recentFavorites != null) {
            FeedbackComponent(
                title = stringResource(R.string.search_idle_message),
                description = stringResource(R.string.search_description_message),
                imageRes = R.drawable.ic_search_recipes
            )
        }
    } else {
        HandlePagingState(pagingItems = recipes, emptyContent = {
            MessageComponent(stringResource(R.string.search_empty_message, searchQuery))
        }, errorContent = {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) { NetworkErrorComponent(onRetry = { recipes.retry() }) }
        }) { loadedRecipes ->
            RecipeGridList(recipes = loadedRecipes, onRecipeClick = onRecipeClick)
        }
    }
}










