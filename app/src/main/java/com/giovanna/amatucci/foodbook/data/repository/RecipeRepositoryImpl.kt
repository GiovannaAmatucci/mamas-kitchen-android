package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.data.paging.RecipePagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipesMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.util.constants.TAG
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [RecipeRepository::class])
class RecipeRepositoryImpl(
    private val api: FatSecretRecipeApi,
    private val mapper: RecipesMapper,
    private val logWriter: LogWriter
) : RecipeRepository {
    override fun searchRecipesPaginated(
        query: String,
        recipeTypes: List<String>?
    ): Flow<PagingData<RecipeItem>> {
        logWriter.d(TAG.RECIPE_REPOSITORY, LogMessages.REPO_PAGER_CREATED.format(query))
        return Pager(
            config = PagingConfig(
                pageSize = RepositoryConstants.RECIPE_REPOSITORY_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = RepositoryConstants.RECIPE_REPOSITORY_PAGE_SIZE
            ), pagingSourceFactory = {
                RecipePagingSource(api, mapper, query, logWriter)
            }
        ).flow
    }

    override suspend fun getRecipeDetails(recipeId: String): ResultWrapper<RecipeDetails> {
        logWriter.d(TAG.RECIPE_REPOSITORY, LogMessages.REPO_DETAILS_REQUEST.format(recipeId))
        api.getRecipeDetails(recipeId).let { apiResult ->
            return when (apiResult) {
                is ResultWrapper.Success -> {
                    try {
                        val recipeDto = apiResult.data.recipe
                        val recipeDetails = mapper.recipeDetailDtoToDomain(recipeDto)
                        logWriter.d(
                            TAG.RECIPE_REPOSITORY,
                            LogMessages.REPO_DETAILS_SUCCESS.format(recipeDetails.id)
                        )
                        ResultWrapper.Success(recipeDetails)
                    } catch (e: Exception) {
                        logWriter.e(
                            TAG.RECIPE_REPOSITORY,
                            LogMessages.REPO_DETAILS_MAPPER_FAILURE.format(e.message)
                        )
                        ResultWrapper.Exception(e)
                    }
                }

                is ResultWrapper.Error -> {
                    logWriter.e(
                        TAG.RECIPE_REPOSITORY, LogMessages.REPO_DETAILS_API_ERROR.format(
                            apiResult.code.toString(), apiResult.message
                        )
                    )
                    apiResult
                }

                is ResultWrapper.Exception -> {
                    logWriter.e(
                        TAG.RECIPE_REPOSITORY,
                        LogMessages.REPO_DETAILS_API_EXCEPTION.format(apiResult.exception.message)
                    )
                    apiResult
                }
            }
        }
    }
}

