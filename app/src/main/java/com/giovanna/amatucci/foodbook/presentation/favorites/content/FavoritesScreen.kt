package com.giovanna.amatucci.foodbook.presentation.favorites.content

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.PagingStateComposable
import com.giovanna.amatucci.foodbook.presentation.components.RecipeList
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState

/**
 * The main content for the Favorites screen.
 * Handles the display of the recipe list and the "Delete All" confirmation dialog.
 *
 * @param uiState The current state of the favorites screen.
 * @param onNavigateToRecipe Callback to navigate to recipe details.
 * @param onEvent Callback for handling user actions (search, delete).
 */
@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onNavigateToRecipe: (recipeId: String) -> Unit,
    onEvent: (FavoritesEvent) -> Unit
) {
    val recipes = uiState.recipes.collectAsLazyPagingItems()

    PagingStateComposable(
        pagingItems = recipes, emptyContent = {
            val message = if (uiState.searchQuery.isBlank()) {
                stringResource(R.string.favorites_empty_message)
            } else {
                stringResource(R.string.favorites_empty_search_message, uiState.searchQuery)
            }
            EmptyMessage(message = message)
        }) { loadedRecipes ->
        RecipeList(
            recipes = loadedRecipes, onRecipeClick = onNavigateToRecipe
        )
    }
    uiState.showConfirmDeleteAllDialog.let { state ->
        if (state) {
            DeleteAllFavoritesDialog(
                onConfirm = { onEvent(FavoritesEvent.ConfirmDeleteAll) },
                onDismiss = { onEvent(FavoritesEvent.DismissDeleteAllConfirmation) })
        }
    }
}
/**
 * A dialog asking the user to confirm the deletion of all favorites.
 *
 * @param onConfirm Callback executed when "Confirm" is clicked.
 * @param onDismiss Callback executed when "Cancel" is clicked or the dialog is dismissed.
 */
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
