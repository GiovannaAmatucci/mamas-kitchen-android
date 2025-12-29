package com.giovanna.amatucci.foodbook.util.constants

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
    }

    object Methods {
        const val RECIPES_SEARCH = "recipes.search.v3"
        const val TOKEN = "connect/token"
        const val REST_RECIPE_V2 = "recipe/v2"
        const val REST_RECIPES_SEARCH_V3 = "recipes/search/v3"

    }

    object Values {
        const val JSON = "json"
        const val CLIENT_CREDENTIALS = "client_credentials"
        const val BASIC = "basic"
    }
}

object RepositoryConstants {
    const val RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX = 1

    const val FAVORITE_REPOSITORY_PAGE_SIZE = 20

    const val RECIPE_REPOSITORY_PAGE_SIZE = 20

    const val AUTH_REPOSITORY_EXPIRES_IN = 1000L

}

object TAG {
    const val RECIPE_REPOSITORY = "RecipeRepository"
    const val RECIPE_PAGING_SOURCE = "RecipePagingSource"
    const val SEARCH_REPOSITORY = "SearchRepository"
    const val AUTH_REPOSITORY = "AuthRepository"
    const val TOKEN_REPOSITORY = "TokenRepository"
    const val FAVORITES_REPOSITORY = "FavoritesRepository"
    const val AUTH_API = "AuthApi"
    const val FAT_SECRET_RECIPE_API = "FatSecretRecipeApi"

    const val NETWORK_HTTP_CLIENT = "NetworkHttpClient"
}

object VIEWMODEL {
    const val FAVORITE_DEBOUNCE = 300L
    const val ARG_RECIPE_ID = "recipeId"
}
