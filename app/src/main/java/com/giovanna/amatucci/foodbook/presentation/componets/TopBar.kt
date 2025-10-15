package com.giovanna.amatucci.foodbook.presentation.componets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.giovanna.amatucci.foodbook.presentation.details.DetailsUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    uiState: DetailsUiState, onNavigateBack: () -> Unit
) {
    TopAppBar(title = {
        val title = when (uiState) {
            is DetailsUiState.Success -> uiState.recipeDetails.title
            else -> ""
        }
        Text(text = title)
    }, navigationIcon = {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar"
            )
        }
    })
}