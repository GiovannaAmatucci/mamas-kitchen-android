package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubCategoriesChips(
    categories: List<String>?, onTagClick: (String) -> Unit, modifier: Modifier = Modifier
) {
    if (categories.isNullOrEmpty()) return
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingExtraSmall)
    ) {
        categories.forEach { category ->
            AssistChip(
                onClick = { onTagClick(category) }, label = {
                    Text(
                        text = category, style = MaterialTheme.typography.labelMedium
                    )
                }, colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.2f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ), border = null
            )
        }
    }
}
