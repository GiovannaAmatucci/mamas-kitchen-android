package com.giovanna.amatucci.foodbook.util.constants
/**
 * Holds constant values related to API endpoints, parameters, and query keys.
 */
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
    const val RECIPE_FATSECRET_STARTING_PAGE_INDEX = 1
    const val SEARCH_OLD_QUERY_ADD_INDEX = 0
    const val NEW_QUERY_TAKE = 10

    const val FAVORITE_PAGE_SIZE = 20

}
