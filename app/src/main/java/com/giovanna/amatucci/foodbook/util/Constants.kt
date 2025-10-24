package com.giovanna.amatucci.foodbook.util

import android.content.Context
import androidx.annotation.StringRes

object LogMessages {
    const val REPO_FETCHING_DETAILS = "Fetching details for recipe ID: %s"
    const val REPO_DETAILS_SUCCESS = "Details found for ID: %s."
    const val REPO_DETAILS_FAILURE_PROPAGATED = "Details failure propagated to domain layer."

    const val REPO_PAGER_CREATED =
        "Repo: Pager created for query '%s'. Paging will now be handled by PagingSource."
    const val PAGING_SOURCE_LOAD_FAILURE_API = "PagingSource: API call failed. Error: %s"

    const val FATSECRET_AUTH_TOKEN_FROM_REPO = "FatSecret: Token carregado do repositório."
    const val FATSECRET_AUTH_TOKEN_NOT_FOUND =
        "FatSecret: Nenhum token de acesso encontrado, retornando null para o plugin Auth."
    const val FATSECRET_AUTH_TOKEN_REFRESHING = "FatSecret: Atualizando token de acesso..."
    const val FATSECRET_AUTH_TOKEN_REFRESHED = "FatSecret: Token atualizado. Novo tipo de token: %s"
    const val FATSECRET_AUTH_TOKEN_REFRESH_FAILED =
        "FatSecret: Falha ao atualizar token de acesso: %s"


    const val AUTH_API_GET_TOKEN_REQUEST = "AuthApi: Solicitando novo token de acesso para %s"
    const val AUTH_API_GET_TOKEN_SUCCESS = "AuthApi: Token de acesso obtido com sucesso."
    const val AUTH_API_GET_TOKEN_FAILURE = "AuthApi: Falha ao obter token de acesso: %s"

    const val FATSECRET_API_SEARCH_RECIPES = "FatSecret: Buscando receitas com expressão: '%s'"
    const val FATSECRET_API_SEARCH_RECIPES_SUCCESS = "FatSecret: Busca de receitas bem-sucedida."
    const val FATSECRET_API_SEARCH_RECIPES_FAILURE_STATUS =
        "FatSecret: Busca de receitas falhou com status HTTP: %s - %s"
    const val FATSECRET_API_SEARCH_RECIPES_EXCEPTION =
        "FatSecret: Busca de receitas falhou com exceção: %s"

    const val FATSECRET_API_GET_RECIPE_DETAILS =
        "FatSecret: Buscando detalhes da receita com ID: '%s'"
    const val FATSECRET_API_GET_RECIPE_DETAILS_SUCCESS =
        "FatSecret: Detalhes da receita obtidos com sucesso."
    const val FATSECRET_API_GET_RECIPE_DETAILS_FAILURE_STATUS =
        "FatSecret: Busca de detalhes da receita falhou com status HTTP: %s - %s"
    const val FATSECRET_API_GET_RECIPE_DETAILS_EXCEPTION =
        "FatSecret: Busca de detalhes da receita falhou com exceção: %s"
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
        @StringRes val resId: Int, vararg val args: Any
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}

