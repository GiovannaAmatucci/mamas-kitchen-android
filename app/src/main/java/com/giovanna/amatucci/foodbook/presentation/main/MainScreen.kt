package com.giovanna.amatucci.foodbook.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.presentation.components.BottomNavigationBar
import com.giovanna.amatucci.foodbook.presentation.favorites.FavoriteScreen
import com.giovanna.amatucci.foodbook.presentation.search.SearchScreen
import com.giovanna.amatucci.foodbook.presentation.search.SearchViewModel
import kotlinx.coroutines.launch

private enum class MainTab { Search, Favorites }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    searchViewModel: SearchViewModel, onNavigateToRecipe: (recipeId: String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { MainTab.entries.size })
    val scope = rememberCoroutineScope()
    Scaffold(
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
            modifier = Modifier.padding(innerPadding),
            beyondViewportPageCount = 1
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    SearchScreen(
                        onNavigateToRecipe = onNavigateToRecipe, viewModel = searchViewModel
                    )
                }

                1 -> {
                    FavoriteScreen(
                        onNavigateToRecipe = onNavigateToRecipe
                    )
                }
            }
        }
    }
}