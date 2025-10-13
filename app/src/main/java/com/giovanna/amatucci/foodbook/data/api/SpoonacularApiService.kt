package com.giovanna.amatucci.foodbook.data.api

import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.model.RecipeInformationDto
import com.giovanna.amatucci.foodbook.data.model.RecipeSearchResponseDto
import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.data.network.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import timber.log.Timber

class SpoonacularApiService(
    private val client: KtorClient
) {
    private val apiKey = BuildConfig.APIKEY_PROPERTIES

    suspend fun searchRecipes(query: String): ApiResult<RecipeSearchResponseDto> {
        return try {
            val response = client().get("recipes/complexSearch") {
                parameter("apiKey", apiKey)
                parameter("query", query)
                parameter("number", 30)
            }.body<RecipeSearchResponseDto>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Failed to search recipes for query: $query")
            ApiResult.Error(e)
        }
    }

    suspend fun getRecipeDetails(id: Int): ApiResult<RecipeInformationDto> {
        return try {
            val response = client().get("recipes/$id/information") {
                parameter("apiKey", apiKey)
            }.body<RecipeInformationDto>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get recipe details for id: $id")
            ApiResult.Error(e)
        }
    }
}

