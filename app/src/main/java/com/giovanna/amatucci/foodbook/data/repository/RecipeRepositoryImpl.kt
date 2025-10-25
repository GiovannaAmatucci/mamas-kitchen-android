package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.data.local.db.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.data.paging.RecipePagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.util.LogMessages
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


class RecipeRepositoryImpl(
    private val api: FatSecretRecipeApi,
    private val dao: SearchDao,
    private val mapper: RecipeDataMapper
) : RecipeRepository {

    override fun searchRecipesPaginated(
        query: String, recipeTypes: List<String>?
    ): Flow<PagingData<RecipeItem>> {
        Timber.d(LogMessages.REPO_PAGER_CREATED.format(query))
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false, initialLoadSize = 20
            ), pagingSourceFactory = {
                RecipePagingSource(api, mapper, query)
            }).flow
    }

    override suspend fun getRecipeDetails(recipeId: String): ResultWrapper<RecipeDetails> {
        Timber.d(LogMessages.REPO_FETCHING_DETAILS.format(recipeId))

        return when (val apiResult = api.getRecipeDetails(recipeId)) {
            is ResultWrapper.Success -> {
                val recipeDto = apiResult.data.recipe
                val recipeDetails = mapper.recipeDetailDtoToDomain(recipeDto)

                Timber.d(LogMessages.REPO_DETAILS_SUCCESS.format(recipeId))
                ResultWrapper.Success(recipeDetails)
            }

            is ResultWrapper.Error -> {
                Timber.e(LogMessages.REPO_DETAILS_FAILURE_PROPAGATED.format(apiResult.code))
                apiResult
            }

            is ResultWrapper.Exception -> {
                Timber.e(LogMessages.REPO_DETAILS_FAILURE_PROPAGATED.format(apiResult.exception))
                apiResult
            }
        }
    }

    override suspend fun saveSearchQuery(query: String) {
        if (query.isBlank()) return
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


