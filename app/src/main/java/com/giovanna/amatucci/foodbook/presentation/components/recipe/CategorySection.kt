package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.domain.model.Category
import com.giovanna.amatucci.foodbook.presentation.components.common.AnimatedChip
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun CategorySection(
    categories: List<Category>, currentQuery: String, onCategoryClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.dimens.paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = AppTheme.dimens.paddingMedium)
    ) {
        items(items = categories, key = { it.title }) { category ->
            val categoryQueryString = stringResource(id = category.query)
            val isSelected = currentQuery.equals(categoryQueryString, ignoreCase = true)
            AnimatedChip(
                category = category,
                isSelected = isSelected,
                onClick = { onCategoryClick(categoryQueryString) })
        }
    }
}