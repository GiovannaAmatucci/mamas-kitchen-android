package com.giovanna.amatucci.foodbook.presentation.navigation.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R

internal sealed class MainScreenNavTab(
    val titleResId: Int, val icon: ImageVector
) {
    data object Search : MainScreenNavTab(
        titleResId = R.string.search_screen_title, icon = Icons.Default.Search
    )

    data object Favorites : MainScreenNavTab(
        titleResId = R.string.favorites_screen_title, icon = Icons.Default.Favorite
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNavigationBar(
    pagerState: PagerState, onTabClick: (Int) -> Unit
) {
    val tabs = listOf(
        MainScreenNavTab.Search, MainScreenNavTab.Favorites
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = pagerState.currentPage == index

            NavigationBarItem(
                label = { Text(text = stringResource(id = tab.titleResId)) }, icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(R.string.navigation_main_screen_navigation_bar)
                    )
                },
                selected = isSelected,
                onClick = {
                    onTabClick(index)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}