package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun GradientScrim(
    modifier: Modifier = Modifier
) {
    val scrimColor = MaterialTheme.colorScheme.scrim
    val alphas = AppTheme.alphas
    val brush = remember(scrimColor, alphas) {
        BrushGradient.verticalScrim(
            color = scrimColor, alphas = alphas
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush)
    )
}