package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeResponse
import com.giovanna.amatucci.foodbook.data.remote.model.search.SearchResponse
import com.giovanna.amatucci.foodbook.data.remote.network.FatSecretApiHttpClient
import com.giovanna.amatucci.foodbook.util.ApiConstants
import com.giovanna.amatucci.foodbook.util.LogMessages
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import timber.log.Timber

interface FatSecretRecipeApi {
    suspend fun searchRecipes(
        expression: String, pageNumber: Int, maxResult: Int
    ): ResultWrapper<SearchResponse>

    suspend fun getRecipeDetails(id: String): ResultWrapper<RecipeResponse>
}

class FatSecretRecipeApiImpl(private val client: FatSecretApiHttpClient) : FatSecretRecipeApi {

    override suspend fun searchRecipes(
        expression: String, pageNumber: Int, maxResult: Int
    ): ResultWrapper<SearchResponse> {
        Timber.d(LogMessages.FATSECRET_API_SEARCH_RECIPES.format(expression))
        return try {
            val response = client().get(ApiConstants.Methods.REST_RECIPES_SEARCH_V3) {
                parameter(ApiConstants.Params.METHOD, ApiConstants.Methods.RECIPES_SEARCH)
                parameter(ApiConstants.Params.PAGE_NUMBER, pageNumber)
                parameter(ApiConstants.Params.FORMAT, ApiConstants.Values.JSON)
                parameter(ApiConstants.Params.MAX_RESULTS, maxResult)
                parameter(ApiConstants.Params.SEARCH_EXPRESSION, expression)
            }

            if (response.status.isSuccess()) {
                val searchResponse: SearchResponse = response.body()
                Timber.d(LogMessages.FATSECRET_API_SEARCH_RECIPES_SUCCESS)
                ResultWrapper.Success(searchResponse)
            } else {
                Timber.e(
                    LogMessages.FATSECRET_API_SEARCH_RECIPES_FAILURE_STATUS.format(
                        response.status.value, response.status.description
                    )
                )
                ResultWrapper.Error(LogMessages.FATSECRET_API_SEARCH_RECIPES_FAILURE_STATUS)
            }
        } catch (e: Exception) {
            Timber.e(
                e, LogMessages.FATSECRET_API_SEARCH_RECIPES_EXCEPTION.format(
                    e.message
                )
            )
            ResultWrapper.Error(
                LogMessages.FATSECRET_API_SEARCH_RECIPES_EXCEPTION, e.message.hashCode()
            )
        }
    }

    override suspend fun getRecipeDetails(id: String): ResultWrapper<RecipeResponse> {
        Timber.d(LogMessages.FATSECRET_API_GET_RECIPE_DETAILS.format(id))
        return try {
            val response = client().get(ApiConstants.Methods.REST_RECIPE_V2) {
                parameter(ApiConstants.Params.RECIPE_ID, id)
                parameter(ApiConstants.Params.FORMAT, ApiConstants.Values.JSON)
            }
            if (response.status.isSuccess()) {
                val recipeResponse: RecipeResponse = response.body()

                Timber.d(LogMessages.FATSECRET_API_GET_RECIPE_DETAILS_SUCCESS)
                ResultWrapper.Success(recipeResponse)
            } else {
                Timber.e(
                    LogMessages.FATSECRET_API_GET_RECIPE_DETAILS_FAILURE_STATUS.format(
                        response.status.value, response.status.description
                    )
                )
                ResultWrapper.Error(LogMessages.FATSECRET_API_GET_RECIPE_DETAILS_FAILURE_STATUS)
            }
        } catch (e: Exception) {
            Timber.e(
                e, LogMessages.FATSECRET_API_GET_RECIPE_DETAILS_EXCEPTION.format(
                    e.message
                )
            )
            ResultWrapper.Error(
                LogMessages.FATSECRET_API_GET_RECIPE_DETAILS_EXCEPTION, e.message.hashCode()
            )
        }
    }
}
