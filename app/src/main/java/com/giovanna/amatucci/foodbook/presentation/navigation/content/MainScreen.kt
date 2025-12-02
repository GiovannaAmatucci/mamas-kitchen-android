package com.giovanna.amatucci.foodbook.presentation.navigation.content

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.favorites.content.FavoritesScreen
import com.giovanna.amatucci.foodbook.presentation.favorites.content.FavoritesTopBar
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState
import com.giovanna.amatucci.foodbook.presentation.search.content.SearchScreen
import com.giovanna.amatucci.foodbook.presentation.search.content.SearchTopBar
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.SearchViewModel
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import kotlinx.coroutines.launch
@Composable
fun MainRoute(
    searchViewModel: SearchViewModel,
    favoriteViewModel: FavoritesViewModel,
    onNavigateToRecipe: (String) -> Unit,
) {
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val favoritesUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        searchUiState = searchUiState,
        favoritesUiState = favoritesUiState,
        onSearchEvent = { searchViewModel.onEvent(it) },
        onFavoriteEvent = { favoriteViewModel.onEvent(it) },
        onNavigateToRecipe = onNavigateToRecipe
    )
}
private enum class MainTab(val tab: Int) { Search(0), Favorites(1) }
@Composable
private fun MainScreen(
    searchUiState: SearchUiState,
    favoritesUiState: FavoritesUiState,
    onSearchEvent: (SearchEvent) -> Unit,
    onFavoriteEvent: (FavoritesEvent) -> Unit,
    onNavigateToRecipe: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { MainTab.entries.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            pagerState.currentPage.let { pager ->
                when (pager) {
                    MainTab.Search.tab -> {
                        SearchTopBar(
                            state = searchUiState, onEvent = { onSearchEvent(it) })
                    }

                    MainTab.Favorites.tab -> {
                        FavoritesTopBar(
                            state = favoritesUiState, onEvent = { onFavoriteEvent(it) })
                    }
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
                MainTab.Search.tab -> {
                    SearchScreen(
                        onNavigateToRecipe = onNavigateToRecipe, state = searchUiState
                    )
                }

                MainTab.Favorites.tab -> {
                    FavoritesScreen(
                        onNavigateToRecipe = onNavigateToRecipe,
                        uiState = favoritesUiState,
                        onEvent = { onFavoriteEvent(it) }
                    )
                }
            }
        }
    }
}

