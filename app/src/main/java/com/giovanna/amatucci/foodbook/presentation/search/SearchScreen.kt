package com.giovanna.amatucci.foodbook.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.componets.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.componets.RecipeList
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchScreen(
    onNavigateToRecipe: (id: Int) -> Unit, viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChange(it) },
            label = { Text(stringResource(R.string.search_field_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.searchRecipes() }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Buscar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
        ) {
            when (val state = uiState) {
                is SearchScreenUiState.Idle -> {
                    EmptyMessage(stringResource(R.string.search_idle_message))

                }

                is SearchScreenUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchScreenUiState.Empty -> {
                    EmptyMessage(stringResource(R.string.search_empty_message))
                }

                is SearchScreenUiState.Error -> {
                    EmptyMessage(state.message.toString())
                }

                is SearchScreenUiState.Success -> {
                    RecipeList(
                        recipes = state.recipes, onRecipeClick = { onNavigateToRecipe(it) })
                }
            }
        }
    }
}