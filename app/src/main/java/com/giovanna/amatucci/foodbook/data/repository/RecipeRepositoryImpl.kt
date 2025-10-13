package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.api.SpoonacularApiService
import com.giovanna.amatucci.foodbook.data.mapper.toDomain
import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
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
            Timber.d("Buscando receitas para a query: '%s'", query)
            when (val apiResult = apiService.searchRecipes(query)) {
                is ApiResult.Success -> {
                    val recipes = apiResult.data.results.map { it.toDomain() }
                    Timber.i("Busca bem-sucedida. Encontradas %d receitas.", recipes.size)
                    ApiResult.Success(recipes)
                }

                is ApiResult.Error -> {
                    Timber.w("Falha na busca de receitas propagada para a camada de domínio.")
                    apiResult
                }
            }
        }

    override suspend fun getRecipeDetails(id: Int): ApiResult<RecipeDetails> =
        withContext(ioDispatcher) {
            Timber.d("Buscando detalhes para o ID da receita: %d", id)
            when (val apiResult = apiService.getRecipeDetails(id)) {
                is ApiResult.Success -> {
                    try {
                        val recipeDetails = apiResult.data.toDomain()
                        Timber.i("Detalhes encontrados para o ID: %d.", id)
                        ApiResult.Success(recipeDetails)
                    } catch (e: Exception) {
                        Timber.e(e, "Falha ao mapear os detalhes da receita para o domínio.")
                        ApiResult.Error(e)
                    }
                }

                is ApiResult.Error -> {
                    Timber.w("Falha na busca de detalhes propagada para a camada de domínio.")
                    apiResult
                }
            }
        }
}
