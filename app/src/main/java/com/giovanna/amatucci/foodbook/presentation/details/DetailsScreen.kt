package com.giovanna.amatucci.foodbook.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.componets.DetailsTopAppBar
import com.giovanna.amatucci.foodbook.presentation.componets.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.componets.LoadingIndicator
import com.giovanna.amatucci.foodbook.presentation.componets.SectionTitle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit, viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
val title = state.recipe?.name?: stringResource(R.string.details_section_title_instructions)
    Scaffold(topBar = {
        DetailsTopAppBar(uiState = title, onNavigateBack = onNavigateBack)
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (state.status) {
                DetailsStatus.Loading -> {
                    LoadingIndicator()
                }

                DetailsStatus.Error -> {
                    EmptyMessage(message = stringResource(R.string.details_error_message_loading_failed))
                }

                DetailsStatus.Success -> {
                    state.recipe?.let { recipe ->
                        RecipeDetailsContent(recipe = recipe)
                    }
                }
            }
        }
    }
}


@Composable
private fun RecipeDetailsContent(recipe: RecipeDetails) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AsyncImage(
                model = recipe.imageUrls?.firstOrNull(), contentDescription = recipe.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recipe.name ?: "",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recipe.description ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SectionTitle(stringResource(R.string.details_section_title_ingredients))
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(recipe.ingredients) { ingredient ->
            Text(
                text = "• ${ingredient.description}",
                modifier = Modifier.padding(bottom = 4.dp, start = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(stringResource(R.string.details_section_title_instructions))
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(recipe.directions) { instruction ->
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = "${instruction.number}.",
                    modifier = Modifier.width(32.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = instruction.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}