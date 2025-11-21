package com.giovanna.amatucci.foodbook.presentation.search.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.AppSearchBarComposable
import com.giovanna.amatucci.foodbook.presentation.components.SearchLeadingIcon
import com.giovanna.amatucci.foodbook.presentation.components.SearchTrailingIcon
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    state: SearchUiState, onEvent: (SearchEvent) -> Unit
) {
    AppSearchBarComposable(
        query = state.searchQuery, isActive = state.isActive,
        placeholder = R.string.search_screen_title,
        onQueryChange = { onEvent(SearchEvent.UpdateSearchQuery(it)) },
        onSearch = { onEvent(SearchEvent.SubmitSearch(it)) },
        onActiveChange = { onEvent(SearchEvent.ActiveChanged(it)) },
        leadingIcon = {
            SearchLeadingIcon(
                isActive = state.isActive,
                onActiveChange = { onEvent(SearchEvent.ActiveChanged(it)) })
        },
        trailingIcon = {
            SearchTrailingIcon(
                query = state.searchQuery, onClearClick = { onEvent(SearchEvent.ClearSearchQuery) })
        },
        content = {
            LazyColumn(
                modifier = Modifier
            ) {
                items(
                    items = state.searchHistory,
                    key = { historyItem -> historyItem }) { historyItem ->
                    ListItem(headlineContent = { Text(historyItem) }, leadingContent = {
                        Icon(
                            Icons.Default.History,
                            contentDescription = stringResource(R.string.search_history_icon_description)
                        )
                    }, modifier = Modifier.clickable {
                        onEvent(SearchEvent.RecentSearchClicked(historyItem))
                    })
                }
            }
            TextButton(
                onClick = { onEvent(SearchEvent.ClearSearchHistory) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall
                    )
            ) {
                Text(
                    text = stringResource(R.string.search_close_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        })
}


