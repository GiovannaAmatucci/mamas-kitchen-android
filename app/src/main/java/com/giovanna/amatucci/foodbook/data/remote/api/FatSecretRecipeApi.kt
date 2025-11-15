package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeResponse
import com.giovanna.amatucci.foodbook.data.remote.model.search.SearchResponse
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.ApiConstants
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface FatSecretRecipeApi {
    suspend fun searchRecipes(
        expression: String, pageNumber: Int, maxResult: Int
    ): ResultWrapper<SearchResponse>

    suspend fun getRecipeDetails(id: String?): ResultWrapper<RecipeResponse>
}

class FatSecretRecipeApiImpl(
    private val client: NetworkHttpClient,
    logWriter: LogWriter
) : BaseApi(logWriter), FatSecretRecipeApi {
    override val TAG: String = "FatSecretRecipeApi"
    override suspend fun searchRecipes(
        expression: String, pageNumber: Int, maxResult: Int
    ): ResultWrapper<SearchResponse> {
        logWriter.d(TAG, LogMessages.API_RECIPE_SEARCH.format(expression, pageNumber))
        return safeApiCall {
            client().get(ApiConstants.Methods.REST_RECIPES_SEARCH_V3) {
                parameter(ApiConstants.Params.METHOD, ApiConstants.Methods.RECIPES_SEARCH)
                parameter(ApiConstants.Params.PAGE_NUMBER, pageNumber)
                parameter(ApiConstants.Params.FORMAT, ApiConstants.Values.JSON)
                parameter(ApiConstants.Params.MAX_RESULTS, maxResult)
                parameter(ApiConstants.Params.SEARCH_EXPRESSION, expression)
            }.body()
        }
    }

    override suspend fun getRecipeDetails(id: String?): ResultWrapper<RecipeResponse> {
        logWriter.d(TAG, LogMessages.API_RECIPE_DETAILS.format(id))
        return safeApiCall {
            client().get(ApiConstants.Methods.REST_RECIPE_V2) {
                parameter(ApiConstants.Params.RECIPE_ID, id)
                parameter(ApiConstants.Params.FORMAT, ApiConstants.Values.JSON)
            }.body()
        }
    }
}