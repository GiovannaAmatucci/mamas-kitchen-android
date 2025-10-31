package com.giovanna.amatucci.foodbook.di.util

import android.content.Context
import androidx.annotation.StringRes

object LogMessages {

    // --- CAMADA DE AUTENTICAÇÃO (AuthApi, AuthRepository) ---

    /** Ação: Iniciando a chamada de rede para buscar um novo token. */
    const val AUTH_TOKEN_REQUEST = "Iniciando requisição de token de acesso..."

    /** Ação: A chamada de rede foi um sucesso e o token foi recebido. */
    const val AUTH_TOKEN_SUCCESS = "Token de acesso recebido da API."

    /** Ação: A chamada de rede falhou. (1 arg: %s = mensagem da exceção) */
    const val AUTH_TOKEN_FAILURE = "Falha ao buscar token de acesso da API. Causa: %s"

    /** Ação: A API retornou o token, mas falhou ao salvá-lo no TokenRepository. (1 arg: %s = mensagem da exceção) */
    const val AUTH_TOKEN_SAVE_FAILURE =
        "API buscou o token, mas falhou ao salvar no repositório. Causa: %s"


    // --- REPOSITÓRIO DE TOKEN (TokenRepository) ---

    /** Ação: Salvando um token no armazenamento local. */
    const val TOKEN_REPO_SAVE = "Salvando token no repositório."

    /** Ação: Lendo o token do armazenamento local. */
    const val TOKEN_REPO_GET = "Buscando token do repositório."

    /** Ação: O token não foi encontrado no armazenamento local. */
    const val TOKEN_REPO_GET_NOT_FOUND = "Nenhum token encontrado no repositório."

    /** Ação: Limpando o token do armazenamento local. */
    const val TOKEN_REPO_CLEAR = "Limpando token do repositório."


    // --- CLIENTE KTOR (Plugin de Auth) ---

    /** Ação: Bloco 'loadTokens' foi chamado e carregou o token com sucesso. */
    const val KTOR_LOAD_TOKEN_SUCCESS = "Token carregado do TokenRepository para a chamada."

    /** Ação: Bloco 'loadTokens' foi chamado, mas não encontrou token. A chamada seguirá desautenticada. */
    const val KTOR_LOAD_TOKEN_FAILURE = "Nenhum token encontrado no TokenRepository para carregar."

    /** Ação: Chamada falhou com 401. Bloco 'refreshTokens' foi ativado. */
    const val KTOR_REFRESH_TOKEN_START =
        "Token expirado (401). Iniciando tentativa de refresh via AuthRepository..."
    const val KTOR_REFRESH_TOKEN_SUCCESS =
        "Token renovado com sucesso. Retentando chamada original."

    /** Ação: O 'refreshTokens' falhou. Limpando credenciais. */
    const val KTOR_REFRESH_TOKEN_FAILURE = "Falha ao renovar o token. Limpando credenciais."


    // --- API (BaseApi - safeApiCall) ---

    /** Erro: A API retornou um erro 4xx. (1 arg: %s = status) */
    const val API_ERROR_CLIENT = "Erro do Cliente (4xx): %s"

    /** Erro: A API retornou um erro 5xx. (1 arg: %s = status) */
    const val API_ERROR_SERVER = "Erro do Servidor (5xx): %s"

    /** Erro: Ocorreu um erro de rede/IO. (1 arg: %s = mensagem da exceção) */
    const val API_ERROR_NETWORK = "Erro de Rede/IO: %s"

    /** Erro: Ocorreu um erro de parsing do JSON. (1 arg: %s = mensagem da exceção) */
    const val API_ERROR_SERIALIZATION = "Erro de Serialização: %s"

    /** Erro: Ocorreu um erro não mapeado. (1 arg: %s = mensagem da exceção) */
    const val API_ERROR_UNEXPECTED = "Erro Inesperado: %s"


    // --- API DE RECEITAS (FatSecretRecipeApi) ---

    /** Ação: Iniciando busca por receitas. (2 args: %s = query, %d = página) */
    const val API_RECIPE_SEARCH = "Buscando receitas. Query: '%s', Página: %s"

    /** Ação: Iniciando busca por detalhes da receita. (1 arg: %s = ID da receita) */
    const val API_RECIPE_DETAILS = "Buscando detalhes da receita. ID: %s"


    // --- PAGING SOURCE (RecipePagingSource) ---

    /** Erro: O PagingSource recebeu um ResultWrapper.Error da API. (2 args: %s = mensagem, %d = código) */
    const val PAGING_LOAD_API_ERROR = "Erro de API no PagingSource. Código: %d, Mensagem: %s"

    /** Erro: O PagingSource recebeu um ResultWrapper.Exception da API. (1 arg: %s = mensagem da exceção) */
    const val PAGING_LOAD_API_EXCEPTION = "Exceção da API no PagingSource: %s"

    /** Erro: O PagingSource capturou uma exceção inesperada no bloco 'try'. (1 arg: %s = mensagem da exceção) */
    const val PAGING_LOAD_UNKNOWN_ERROR = "Erro inesperado no PagingSource.load: %s"


    // --- REPOSITÓRIO DE RECEITAS (RecipeRepository) ---

    /** Ação: O Pager e o PagingSource foram criados. (1 arg: %s = query) */
    const val REPO_PAGER_CREATED = "Pager e PagingSource criados para a query: '%s'"

    /** Ação: O repositório foi chamado para buscar detalhes. (1 arg: %s = ID da receita) */
    const val REPO_DETAILS_REQUEST = "Buscando detalhes no repositório para o ID: %s"

    /** Ação: O repositório recebeu dados da API e mapeou para o domínio com sucesso. (1 arg: %s = ID da receita) */
    const val REPO_DETAILS_SUCCESS = "Detalhes encontrados e mapeados para o domínio. ID: %s"

    /** Erro: O repositório recebeu um ResultWrapper.Error da API. (2 args: %s = mensagem, %d = código) */
    const val REPO_DETAILS_API_ERROR = "Erro da API ao buscar detalhes. Código: %s, Mensagem: %s"

    /** Erro: O repositório recebeu um ResultWrapper.Exception da API. (1 arg: %s = mensagem da exceção) */
    const val REPO_DETAILS_API_EXCEPTION = "Exceção da API ao buscar detalhes: %s"

    /** Ação: Salvando uma nova query no histórico de busca. (1 arg: %s = query) */
    const val REPO_SAVE_SEARCH = "Salvando nova query de busca no DAO: '%s'"
}

object ApiConstants {

    object Params {
        const val METHOD = "method"
        const val FORMAT = "format"
        const val PAGE_NUMBER = "page_number"
        const val MAX_RESULTS = "max_results"

        const val SEARCH_EXPRESSION = "search_expression"
        const val RECIPE_ID = "recipe_id"
        const val GRANT_TYPE = "grant_type"
        const val SCOPE = "scope"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
    }

    object Methods {
        const val RECIPES_SEARCH = "recipes.search.v3"
        const val TOKEN = "connect/token"

        const val REST_RECIPE_V2 = "rest/recipe/v2"

        const val REST_RECIPES_SEARCH_V3 = "rest/recipes/search/v3"

    }

    object Values {
        const val JSON = "json"
        const val CLIENT_CREDENTIALS = "client_credentials"
        const val BASIC = "basic"
    }
}

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int, vararg val args: Int?
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}

