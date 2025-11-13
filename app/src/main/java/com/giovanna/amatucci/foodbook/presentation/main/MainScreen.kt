package com.giovanna.amatucci.foodbook.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.favorites.FavoriteScreen
import com.giovanna.amatucci.foodbook.presentation.favorites.FavoritesTopBar
import com.giovanna.amatucci.foodbook.presentation.favorites.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.search.SearchScreen
import com.giovanna.amatucci.foodbook.presentation.search.SearchTopBar
import com.giovanna.amatucci.foodbook.presentation.search.SearchViewModel
import kotlinx.coroutines.launch

private enum class MainTab { Search, Favorites }

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    searchViewModel: SearchViewModel,
    favoriteViewModel: FavoritesViewModel,
    onNavigateToRecipe: (recipeId: String) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { MainTab.entries.size })
    val scope = rememberCoroutineScope()
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val history by searchViewModel.searchHistory.collectAsStateWithLifecycle()
    val favoritesUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            when (pagerState.currentPage) {
                0 -> {
                    SearchTopBar(
                        query = searchUiState.searchQuery,
                        isActive = searchUiState.isActive,
                        history = history,
                        onEvent = { searchViewModel.onEvent(it) })
                }

                1 -> {
                    FavoritesTopBar(
                        query = favoritesUiState.searchQuery,
                        onEvent = { favoriteViewModel.onEvent(it) })
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                pagerState = pagerState, onTabClick = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
            contentPadding = innerPadding,
            beyondViewportPageCount = 1
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    SearchScreen(
                        onNavigateToRecipe = onNavigateToRecipe, state = searchUiState
                    )
                }
                1 -> {
                    FavoriteScreen(
                        onNavigateToRecipe = onNavigateToRecipe,
                        uiState = favoritesUiState,
                        onEvent = { favoriteViewModel.onEvent(it) }
                    )
                }
            }
        }
    }
}

