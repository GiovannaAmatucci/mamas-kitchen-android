package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.giovanna.amatucci.foodbook.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    uiState: String,
    onNavigateBack: () -> Unit,
    action: () -> Unit,
    imageVector: ImageVector,
    tint: Color
) {
    TopAppBar(title = {
        Text(uiState)
    }, navigationIcon = {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = R.string.common_button_back.toString()
            )
        }
    }, actions = {
        IconButton(onClick = action) {
            Icon(
                imageVector = imageVector, contentDescription = "Favoritar", tint = tint
            )

        }
    })
}