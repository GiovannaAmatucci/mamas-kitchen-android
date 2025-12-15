package com.giovanna.amatucci.foodbook.presentation.favorites.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.MessageComponent
import com.giovanna.amatucci.foodbook.presentation.components.feedback.FeedbackComponent
import com.giovanna.amatucci.foodbook.presentation.components.feedback.HandlePagingState
import com.giovanna.amatucci.foodbook.presentation.components.feedback.NetworkErrorComponent
import com.giovanna.amatucci.foodbook.presentation.components.recipe.RecipeGridList
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState

@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
    onNavigateToRecipe: (recipeId: String) -> Unit,
    onEvent: (FavoritesEvent) -> Unit
) {
    val recipes = state.recipes.collectAsLazyPagingItems()

    state.apply {
        FavoritesContent(
            searchQuery = searchQuery,
            recipes = recipes,
            onNavigateToRecipe = onNavigateToRecipe,
            hasAnyFavorite = hasAnyFavorite
        )

        if (showConfirmDeleteAllDialog) {
            DeleteAllFavoritesDialog(
                onConfirm = { onEvent(FavoritesEvent.ConfirmDeleteAll) },
                onDismiss = { onEvent(FavoritesEvent.DismissDeleteAllConfirmation) }
            )
        }
    }
}

@Composable
private fun FavoritesContent(
    searchQuery: String,
    recipes: LazyPagingItems<RecipeItem>,
    onNavigateToRecipe: (String) -> Unit,
    hasAnyFavorite: Boolean
) {
    HandlePagingState(
        pagingItems = recipes,
        emptyContent = {
            if (!hasAnyFavorite && searchQuery.isBlank()) {
                FeedbackComponent(
                    title = stringResource(R.string.no_favorites_yet),
                    description = stringResource(R.string.no_favorites_description),
                    imageRes = R.drawable.ic_favorites_recipe,
                    subTitleColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                MessageComponent(
                    message = stringResource(R.string.favorites_empty_search_message, searchQuery)
                )
            }
        },
        errorContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NetworkErrorComponent(onRetry = { recipes.retry() })
            }
        }
    ) { loadedRecipes ->
        RecipeGridList(
            recipes = loadedRecipes,
            onRecipeClick = onNavigateToRecipe
        )
    }
}

@Composable
private fun DeleteAllFavoritesDialog(
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.favorites_dialog_title)) },
        text = { Text(stringResource(R.string.favorites_dialog_text)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.common_button_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_button_cancel))
            }
        },
        shape = MaterialTheme.shapes.medium
    )
}