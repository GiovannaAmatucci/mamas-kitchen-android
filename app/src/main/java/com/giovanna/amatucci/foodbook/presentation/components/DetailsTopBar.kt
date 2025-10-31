package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.giovanna.amatucci.foodbook.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    uiState: String, onNavigateBack: () -> Unit
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
    })
}