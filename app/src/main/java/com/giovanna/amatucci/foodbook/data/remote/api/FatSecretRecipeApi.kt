package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeResponse
import com.giovanna.amatucci.foodbook.data.remote.model.search.SearchResponse
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.ApiConstants
import com.giovanna.amatucci.foodbook.util.constants.LogMessages.API_RECIPE_DETAILS
import com.giovanna.amatucci.foodbook.util.constants.LogMessages.API_RECIPE_SEARCH
import com.giovanna.amatucci.foodbook.util.constants.TAG.FAT_SECRET_RECIPE_API
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Single

interface FatSecretRecipeApi {
    suspend fun searchRecipes(
        expression: String,
        pageNumber: Int,
        maxResult: Int
    ): ResultWrapper<SearchResponse>

    suspend fun getRecipeDetails(id: String?): ResultWrapper<RecipeResponse>
}
@Single(binds = [FatSecretRecipeApi::class])
class FatSecretRecipeApiImpl(
    private val client: NetworkHttpClient,
    logWriter: LogWriter
) : BaseApi(logWriter), FatSecretRecipeApi {
    override val tag: String = FAT_SECRET_RECIPE_API
    override suspend fun searchRecipes(
        expression: String,
        pageNumber: Int,
        maxResult: Int
    ): ResultWrapper<SearchResponse> {
        logWriter.d(tag, API_RECIPE_SEARCH.format(expression, pageNumber, maxResult))
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
        logWriter.d(tag, API_RECIPE_DETAILS.format(id))
        return safeApiCall {
            client().get(ApiConstants.Methods.REST_RECIPE_V2) {
                parameter(ApiConstants.Params.RECIPE_ID, id)
                parameter(ApiConstants.Params.FORMAT, ApiConstants.Values.JSON)
            }.body()
        }
    }
}