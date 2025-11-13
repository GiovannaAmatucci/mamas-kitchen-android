package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.data.local.db.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.data.paging.RecipePagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow


class RecipeRepositoryImpl(
    private val api: FatSecretRecipeApi,
    private val dao: SearchDao,
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
                    try {
                        val recipeDto = apiResult.data.recipe
                        val recipeDetails = mapper.recipeDetailDtoToDomain(recipeDto)

                        logWriter.d(TAG, LogMessages.REPO_DETAILS_SUCCESS.format(recipeDetails.id))
                        ResultWrapper.Success(recipeDetails)

                    } catch (e: Exception) {
                        logWriter.e(TAG, LogMessages.REPO_DETAILS_MAPPER_FAILURE.format(e.message))
                        ResultWrapper.Exception(e)
                    }
                }

                is ResultWrapper.Error -> {
                    logWriter.e(
                        TAG, LogMessages.REPO_DETAILS_API_ERROR.format(
                            apiResult.code.toString(), apiResult.message
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
        if (query.isNotBlank()) {
            val currentHistory = dao.getSearchHistory()
            val oldQueries = currentHistory?.queries?.toMutableList() ?: mutableListOf()
            oldQueries.remove(query)
            oldQueries.add(0, query)
            val newQueries = oldQueries.take(10)
            val newHistory = SearchEntity(id = currentHistory?.id ?: 0, queries = newQueries)
            dao.insertSearch(newHistory)
        }
    }

    override suspend fun getSearchQueries(): List<String> {
        val currentHistory = dao.getSearchHistory()
        if (currentHistory?.queries.isNullOrEmpty()) {
            return emptyList()
        }
        return currentHistory.queries
    }

    override suspend fun clearSearchHistory() {
        val currentHistory = dao.getSearchHistory()
        val emptyHistory = SearchEntity(
            id = currentHistory?.id ?: 0, queries = emptyList()
        )
        dao.insertSearch(emptyHistory)
    }
}

