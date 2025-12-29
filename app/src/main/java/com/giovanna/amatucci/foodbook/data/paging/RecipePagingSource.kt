package com.giovanna.amatucci.foodbook.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipesMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.util.constants.TAG

class RecipePagingSource(
    private val api: FatSecretRecipeApi,
    private val mapper: RecipesMapper,
    private val query: String,
    private val logWriter: LogWriter
) : PagingSource<Int, RecipeItem>() {
    private val index = RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeItem> {
        val position = params.key ?: RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX
        val maxResults = params.loadSize
        return try {
            api.searchRecipes(query, position, maxResults).let { apiResult ->
                when (apiResult) {
                    is ResultWrapper.Success -> {
                        val recipes = apiResult.data.recipesSearch?.recipeSearch ?: emptyList()
                        val domainData = recipes.map { mapper.searchRecipeDtoToDomain(it) }

                        LoadResult.Page(
                            data = domainData,
                            prevKey = if (position == index) null else position - index,
                            nextKey = if (recipes.isEmpty()) null else position + index
                        )
                    }

                    is ResultWrapper.Error -> {
                        val msg = LogMessages.PAGING_LOAD_API_ERROR
                            .format(apiResult.message, apiResult.code)
                        logWriter.e(TAG.RECIPE_PAGING_SOURCE, msg)
                        LoadResult.Error(Exception(apiResult.message))
                    }

                    is ResultWrapper.Exception -> {
                        val msg = LogMessages.PAGING_LOAD_API_EXCEPTION.format(
                            apiResult.exception.message
                        )
                        logWriter.e(TAG.RECIPE_PAGING_SOURCE, msg)
                        LoadResult.Error(apiResult.exception)
                    }
                }
            }
        } catch (e: Exception) {
            val msg = LogMessages.PAGING_LOAD_UNKNOWN_ERROR.format(e.message)
            logWriter.e(TAG.RECIPE_PAGING_SOURCE, msg, e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(index)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(index)
        }
}
