package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNavTopAppBar(
    query: String,
    isActive: Boolean,
    placeholder: Int,
    expandable: Boolean = true,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit) = {}
) {
    val barIsActive = expandable && isActive
    val onBarActiveChange: (Boolean) -> Unit = if (expandable) onActiveChange else { _ -> }
    val modifier = if (isActive && expandable) Modifier.fillMaxWidth()
    else Modifier
        .fillMaxWidth()
        .padding(all = 12.dp)

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = barIsActive,
        onActiveChange = onBarActiveChange,
        placeholder = { Text(stringResource(placeholder)) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = MaterialTheme.shapes.medium,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface,
            dividerColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        content = content
    )
}









