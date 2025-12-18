package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun CollapsibleSectionChips(
    title: String, isExpanded: Boolean, onToggleClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimens.paddingMedium, vertical = AppTheme.dimens.paddingSmall
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(AppTheme.dimens.weightDefault))
        IconButton(
            onClick = onToggleClick, modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.FilterList,
                contentDescription = stringResource(R.string.search_toggle_section),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}