package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.api.SpoonacularApiService
import com.giovanna.amatucci.foodbook.data.mapper.toDomain
import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.util.LogMessages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RecipeRepositoryImpl(
    private val apiService: SpoonacularApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {

    override suspend fun searchRecipes(query: String): ApiResult<List<RecipeSummary>> =
        withContext(ioDispatcher) {
            val apiResult = apiService.searchRecipes(query)
            Timber.d(LogMessages.REPO_SEARCHING_RECIPES, query)
            when (apiResult) {
                is ApiResult.Success -> {
                    val recipes = apiResult.data.results.map { it.toDomain() }
                    Timber.i(LogMessages.REPO_SEARCH_SUCCESS, recipes.size)
                    ApiResult.Success(recipes)
                }

                is ApiResult.Error -> {
                    Timber.w(LogMessages.REPO_SEARCH_FAILURE_PROPAGATED)
                    apiResult
                }
            }
        }

    override suspend fun getRecipeDetails(id: Int): ApiResult<RecipeDetails> =
        withContext(ioDispatcher) {
            val apiResult = apiService.getRecipeDetails(id)
            Timber.d(LogMessages.REPO_FETCHING_DETAILS, id)
            when (apiResult) {
                is ApiResult.Success -> {
                    try {
                        val recipeDetails = apiResult.data.toDomain()
                        Timber.i(LogMessages.REPO_DETAILS_SUCCESS, id)
                        ApiResult.Success(recipeDetails)
                    } catch (e: Exception) {
                        Timber.e(e, LogMessages.REPO_MAPPING_ERROR)
                        ApiResult.Error(e)
                    }
                }

                is ApiResult.Error -> {
                    Timber.w(LogMessages.REPO_DETAILS_FAILURE_PROPAGATED)
                    apiResult
                }
            }
        }
}
