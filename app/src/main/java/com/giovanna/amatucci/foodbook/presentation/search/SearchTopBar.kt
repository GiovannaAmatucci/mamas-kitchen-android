package com.giovanna.amatucci.foodbook.presentation.search

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.SearchNavTopAppBar
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

@Composable
fun SearchTopBar(
    query: String, isActive: Boolean, onEvent: (SearchEvent) -> Unit, history: List<String>
) {
    SearchNavTopAppBar(
        query = query,
        isActive = isActive,
        placeholder = R.string.search_screen_title,
        onQueryChange = { onEvent(SearchEvent.UpdateSearchQuery(it)) },
        onSearch = { onEvent(SearchEvent.SubmitSearch(it)) },
        onActiveChange = { onEvent(SearchEvent.ActiveChanged(it)) },
        leadingIcon = {
            SearchLeadingIcon(
                isActive = isActive, onActiveChange = { onEvent(SearchEvent.ActiveChanged(it)) })
        },
        trailingIcon = {
            SearchTrailingIcon(
                query = query, onClearClick = { onEvent(SearchEvent.ClearSearchQuery) })
        },
        content = {
            LazyColumn(
                modifier = Modifier
            ) {
                items(
                    items = history, key = { historyItem -> historyItem }) { historyItem ->
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


@Composable
private fun SearchTrailingIcon(query: String, onClearClick: () -> Unit) {
    if (query.isNotEmpty()) IconButton(onClick = { onClearClick() }) {
        Icon(
            Icons.Default.Close,
            contentDescription = stringResource(R.string.search_close_description)

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