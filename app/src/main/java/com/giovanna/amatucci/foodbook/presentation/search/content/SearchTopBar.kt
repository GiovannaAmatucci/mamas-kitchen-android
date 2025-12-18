package com.giovanna.amatucci.foodbook.presentation.search.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.search.SearchBarWrapper
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    state: SearchUiState, onEvent: (SearchEvent) -> Unit
) {
    state.apply {
        SearchBarWrapper(
            query = searchQuery,
            isActive = isActive,
            placeholder = R.string.search_screen_title,
            onQueryChange = { onQueryChange -> onEvent(SearchEvent.UpdateSearchQuery(onQueryChange)) },
            onSearch = { onSearch -> onEvent(SearchEvent.SubmitSearch(onSearch)) },
            onActiveChange = { onActiveChange -> onEvent(SearchEvent.ActiveChanged(onActiveChange)) },
            leadingIcon = {
                SearchLeadingIcon(
                    isActive = isActive, onActiveChange = { onActiveChange ->
                        onEvent(SearchEvent.ActiveChanged(onActiveChange))
                    }
                )
            },
            trailingIcon = {
                SearchTrailingIcon(
                    query = searchQuery, onClearClick = { onEvent(SearchEvent.ClearSearchQuery) })
            },
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = searchHistory, key = { it }) { historyItem ->
                        ListItem(
                            headlineContent = { Text(historyItem) },
                            leadingContent = {
                                Icon(
                                    Icons.Default.History,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.clickable {
                                onEvent(SearchEvent.RecentSearchClicked(historyItem))
                            }
                        )
                    }
                    if (searchHistory.isNotEmpty()) {
                        item {
                            TextButton(
                                onClick = { onEvent(SearchEvent.ClearSearchHistory) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = AppTheme.dimens.paddingMedium,
                                        vertical = AppTheme.dimens.paddingSmall
                                    )
                            ) {
                                SectionTitle(
                                    title = stringResource(R.string.search_close_description),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun SearchLeadingIcon(
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
) {
    if (isActive) IconButton(onClick = { onActiveChange(false) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.common_button_back)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_screen_title)
        )
    }
}

@Composable
private fun SearchTrailingIcon(query: String, onClearClick: () -> Unit) {
    val isVisible = query.isNotEmpty()
    IconButton(
        onClick = { if (isVisible) onClearClick() },
        enabled = isVisible
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.search_close_description),
            tint = if (isVisible) MaterialTheme.colorScheme.onSurface else Color.Transparent
        )
    }
}


