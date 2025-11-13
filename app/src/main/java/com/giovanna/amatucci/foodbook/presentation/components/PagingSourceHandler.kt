package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.giovanna.amatucci.foodbook.R

/**
 * Um componente genérico que lida com os estados comuns de um LazyPagingItems.
 * Exibe conteúdo para os estados de Carregamento, Erro e Lista Vazia.
 *
 * @param T O tipo de item na lista de paginação.
 * @param pagingItems O LazyPagingItems cujo estado será observado.
 * @param loadingContent O Composable a ser exibido durante o carregamento inicial.
 * @param errorContent O Composable a ser exibido em caso de erro.
 * @param emptyContent O Composable a ser exibido quando a lista carrega com sucesso, mas está vazia.
 * @param content O Composable principal a ser exibido quando há itens na lista.
 */

@Composable
fun <T : Any> PagingStateHandler(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    loadingContent: @Composable () -> Unit = { LoadingIndicator() },
    errorContent: @Composable (message: String) -> Unit = { EmptyMessage(message = it) },
    emptyContent: @Composable () -> Unit = { EmptyMessage(message = stringResource(R.string.search_empty_message)) },
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

                is LoadState.NotLoading if pagingItems.itemCount == 0 ->
                    emptyContent()


                else -> {
                    content(pagingItems)
                }
            }
        }
    }
}