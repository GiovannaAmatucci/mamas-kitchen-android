package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.data.local.db.AppDatabase
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.data.paging.RecipePagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.di.util.LogMessages
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow


class RecipeRepositoryImpl(
    private val api: FatSecretRecipeApi,
    private val dbDeferred: Deferred<AppDatabase>,
    private val mapper: RecipeDataMapper,
    private val logWriter: LogWriter
) : RecipeRepository {
    companion object {
        private const val TAG = "RecipeRepository"
    }

    override fun searchRecipesPaginated(
        query: String, recipeTypes: List<String>?
    ): Flow<PagingData<RecipeItem>> {
        logWriter.d(TAG, LogMessages.REPO_PAGER_CREATED.format(query))
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false, initialLoadSize = 20
            ), pagingSourceFactory = {
                RecipePagingSource(api, mapper, query, logWriter)
            }).flow
    }

    override suspend fun getRecipeDetails(recipeId: String): ResultWrapper<RecipeDetails> {
        logWriter.d(TAG, LogMessages.REPO_DETAILS_REQUEST.format(recipeId))
        api.getRecipeDetails(recipeId).let { apiResult ->
            return when (apiResult) {
                is ResultWrapper.Success -> {
                    val recipeDto = apiResult.data.recipe
                    val recipeDetails = mapper.recipeDetailDtoToDomain(recipeDto)

                    logWriter.d(TAG, LogMessages.REPO_DETAILS_SUCCESS.format(recipeDetails.id))
                    ResultWrapper.Success(recipeDetails)
                }

                is ResultWrapper.Error -> {
                    logWriter.e(
                        TAG, message = LogMessages.REPO_DETAILS_API_ERROR.format(
                            apiResult.code, apiResult.message
                        )
                    )
                    apiResult
                }

                is ResultWrapper.Exception -> {
                    logWriter.e(
                        TAG,
                        LogMessages.REPO_DETAILS_API_EXCEPTION.format(apiResult.exception.message)
                    )
                    apiResult
                }
            }
        }
    }

    override suspend fun saveSearchQuery(query: String) {
        if (query.isBlank()) return
        val dao = dbDeferred.await().searchDao()
        val currentHistory = dao.getSearchHistory()
        val currentQueries = currentHistory?.queries ?: emptyList()
        val newQueries = buildList {
            add(query)
            addAll(currentQueries.filterNot { it == query })
        }.take(10)
        val newHistory = SearchEntity(id = currentHistory?.id ?: 0, queries = newQueries)
        dao.insertSearch(newHistory)
    }
}


