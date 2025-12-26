package com.giovanna.amatucci.foodbook.presentation.search.content

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.Category
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.components.common.CollapsibleSectionChips
import com.giovanna.amatucci.foodbook.presentation.components.feedback.NetworkErrorComponent
import com.giovanna.amatucci.foodbook.presentation.components.recipe.CategorySection
import com.giovanna.amatucci.foodbook.presentation.components.search.SearchInitialSection
import com.giovanna.amatucci.foodbook.presentation.components.search.SearchList
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchUiState, onNavigateToRecipe: (id: String) -> Unit, onEvent: (SearchEvent) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var areFiltersExpanded by rememberSaveable(isLandscape) { mutableStateOf(!isLandscape) }
    SearchScreenContent(
        state = state,
        isLandscape = isLandscape,
        areFiltersExpanded = areFiltersExpanded,
        onToggleFilters = { areFiltersExpanded = !areFiltersExpanded },
        onEvent = onEvent,
        onNavigate = onNavigateToRecipe
    )
}

@Composable
private fun SearchScreenContent(
    state: SearchUiState,
    isLandscape: Boolean,
    areFiltersExpanded: Boolean,
    onToggleFilters: () -> Unit,
    onEvent: (SearchEvent) -> Unit,
    onNavigate: (String) -> Unit,
) {
    state.apply {
        Box(modifier = Modifier.fillMaxSize()) {
            when (status) {
                is ScreenStatus.Loading -> {
                    SearchScreenShimmer()
                }

                is ScreenStatus.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        NetworkErrorComponent(onRetry = { onEvent(SearchEvent.Retry) })
                    }
                }

                is ScreenStatus.Success -> {
                    SearchScreenContentSuccess(
                        searchQuery = submittedQuery,
                        categories = categories,
                        recipes = recipes.collectAsLazyPagingItems(),
                        recentFavorites = lastFavorites,
                        isLandscape = isLandscape,
                        areFiltersExpanded = areFiltersExpanded,
                        onToggleFilters = onToggleFilters,
                        onCategoryClick = { query ->
                            val newQuery = if (submittedQuery != query) query else ""
                            onEvent(SearchEvent.UpdateSearchQuery(newQuery))
                            onEvent(SearchEvent.SubmitSearch(newQuery))
                        },
                        onNavigateToRecipe = onNavigate
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchScreenContentSuccess(
    searchQuery: String,
    categories: List<Category>,
    recipes: LazyPagingItems<RecipeItem>,
    recentFavorites: List<RecipeItem>?,
    isLandscape: Boolean,
    areFiltersExpanded: Boolean,
    onToggleFilters: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigateToRecipe: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        if (isLandscape) {
            CollapsibleSectionChips(
                title = stringResource(R.string.search_categories),
                isExpanded = areFiltersExpanded,
                onToggleClick = onToggleFilters
            )
        }
        AnimatedVisibility(
            visible = areFiltersExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            CategorySection(
                categories = categories,
                currentQuery = searchQuery,
                onCategoryClick = onCategoryClick
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(AppTheme.dimens.weightDefault)
        ) {
            if (searchQuery.isBlank()) {
                SearchInitialSection(
                    recentFavorites = recentFavorites,
                    onRecipeClick = onNavigateToRecipe
                )
            } else {
                SearchList(
                    searchQuery = searchQuery, recipes = recipes,
                    onRecipeClick = onNavigateToRecipe
                )
            }
        }
    }
}