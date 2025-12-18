package com.giovanna.amatucci.foodbook.presentation.components.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.common.MessageComponent
import com.giovanna.amatucci.foodbook.presentation.components.feedback.shimmer.RecipeCardShimmer

/**
 * A generic component that handles common states of a [LazyPagingItems] list.
 * Displays Shimmer for Loading, Error Message for errors, and Empty Message for empty lists.
 *
 * @param T The type of item in the paging list.
 * @param pagingItems The [LazyPagingItems] to observe state from.
 * @param modifier The modifier to be applied to the container.
 * @param loadingContent Composable to display during initial loading (Defaults to 6 Shimmer Cards).
 * @param errorContent Composable to display when an error occurs.
 * @param emptyContent Composable to display when the list loads successfully but is empty.
 * @param content The main Composable to display when there are items (Success state).
 */
@Composable
fun <T : Any> HandlePagingState(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = {
        Column {
            repeat(6) { RecipeCardShimmer() }
        }
    },
    errorContent: @Composable (message: String) -> Unit = { MessageComponent(message = it) },
    emptyContent: @Composable () -> Unit = { MessageComponent(message = stringResource(R.string.search_empty_message)) },
    content: @Composable (pagingItems: LazyPagingItems<T>) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        pagingItems.loadState.refresh.let { paging ->
            when (paging) {
                is LoadState.Loading -> {
                    loadingContent()
                }

                is LoadState.Error -> {
                    errorContent(stringResource(R.string.search_error_message_loading_failed))
                }

                is LoadState.NotLoading -> {
                    if (pagingItems.itemCount == 0 && pagingItems.loadState.append.endOfPaginationReached) {
                        emptyContent()
                    } else {
                        content(pagingItems)
                    }
                }
            }
        }
    }
}