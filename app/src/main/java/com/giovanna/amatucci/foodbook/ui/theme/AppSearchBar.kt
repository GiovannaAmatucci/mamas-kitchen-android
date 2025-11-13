package com.giovanna.amatucci.foodbook.ui.theme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
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
            if (isActive && expandable) Modifier
            else Modifier.padding(all = Dimens.PaddingMedium)
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









