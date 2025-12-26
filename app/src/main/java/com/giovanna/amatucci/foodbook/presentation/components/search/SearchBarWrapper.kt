package com.giovanna.amatucci.foodbook.presentation.components.search

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

/**
 * A customized Search Bar component wrapper around Material3 SearchBar.
 *
 * @param query The text currently shown in the search field.
 * @param isActive Whether the search bar is currently active (expanded/focused).
 * @param placeholder The string resource ID for the placeholder text when the query is empty.
 * @param expandable Whether the search bar can expand into a full view. Defaults to true.
 * @param onQueryChange Callback triggered when the input text changes.
 * @param onSearch Callback triggered when the search action is submitted (e.g., IME action).
 * @param onActiveChange Callback triggered when the active state changes.
 * @param shape The shape of the search bar container.
 * @param colors The colors used for the search bar (container, divider, etc.).
 * @param trailingIcon Composable to be displayed at the end of the input field.
 * @param leadingIcon Composable to be displayed at the start of the input field.
 * @param content The content to be displayed below the search bar when active (e.g., search results).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWrapper(
    query: String,
    isActive: Boolean,
    @StringRes placeholder: Int,
    expandable: Boolean = true,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    shape: Shape = MaterialTheme.shapes.medium,
    colors: SearchBarColors = SearchBarDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surface,
        dividerColor = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    trailingIcon: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit) = {}
) {
    val modifier = Modifier
        .fillMaxWidth()
        .then(
            if (isActive && expandable) Modifier else Modifier.padding(all = AppTheme.dimens.paddingMedium)
        )
    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = expandable && isActive,
        onActiveChange = if (expandable) onActiveChange else { _ -> },
        placeholder = { Text(stringResource(placeholder)) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        content = content
    )
}









