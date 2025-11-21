package com.giovanna.amatucci.foodbook.presentation.navigation.content

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.presentation.favorites.content.FavoritesRouteContent
import com.giovanna.amatucci.foodbook.presentation.favorites.content.FavoritesTopBar
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState
import com.giovanna.amatucci.foodbook.presentation.search.content.SearchRouteContent
import com.giovanna.amatucci.foodbook.presentation.search.content.SearchTopBar
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import kotlinx.coroutines.launch

private enum class MainTab(val tab: Int) { Search(0), Favorites(1) }
@Composable
fun MainScreen(
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
                    SearchRouteContent(
                        onNavigateToRecipe = onNavigateToRecipe, state = searchUiState
                    )
                }

                MainTab.Favorites.tab -> {
                    FavoritesRouteContent(
                        onNavigateToRecipe = onNavigateToRecipe,
                        uiState = favoritesUiState,
                        onEvent = { onFavoriteEvent(it) }
                    )
                }
            }
        }
    }
}

