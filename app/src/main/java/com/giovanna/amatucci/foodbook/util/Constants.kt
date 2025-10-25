package com.giovanna.amatucci.foodbook.util

object LogMessages {
    // SpoonacularApiService
    const val API_SEARCH_FAILED = "Failed to search recipes for query: %s"
    const val API_DETAILS_FAILED = "Failed to get recipe details for id: %d"

    // RecipeRepositoryImpl
    const val REPO_SEARCHING_RECIPES = "Searching recipes for query: '%s'"
    const val REPO_SEARCH_SUCCESS = "Search successful. Found %d recipes."
    const val REPO_SEARCH_FAILURE_PROPAGATED = "Search failure propagated to domain layer."
    const val REPO_FETCHING_DETAILS = "Fetching details for recipe ID: %d"
    const val REPO_DETAILS_SUCCESS = "Details found for ID: %d."
    const val REPO_DETAILS_FAILURE_PROPAGATED = "Details failure propagated to domain layer."
    const val REPO_MAPPING_ERROR = "Failed to map recipe details to domain."
}
