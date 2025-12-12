package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun MessageComponent(message: String, modifier: Modifier = Modifier, color: Color? = null) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimens.paddingMedium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = color ?: MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
