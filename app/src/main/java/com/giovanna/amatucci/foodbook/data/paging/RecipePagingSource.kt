package com.giovanna.amatucci.foodbook.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.di.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem


class RecipePagingSource(
    private val api: FatSecretRecipeApi,
    private val mapper: RecipeDataMapper,
    private val query: String, private val logWriter: LogWriter
) : PagingSource<Int, RecipeItem>() {
    companion object {
        private const val TAG = "RecipePagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeItem> {
        val position = params.key ?: RepositoryConstants.FATSECRET_STARTING_PAGE_INDEX
        val maxResults = params.loadSize

        return try {
            api.searchRecipes(query, position, maxResults).let { apiResult ->
                when (apiResult) {
                    is ResultWrapper.Success -> {
                        val recipesSearch = apiResult.data.recipesSearch
                        val recipeDtos = recipesSearch?.recipeSearch ?: emptyList()
                        val domainData = recipeDtos.map { mapper.searchRecipeDtoToDomain(it) }
                        val nextKey = if (recipeDtos.isEmpty()) null else position + 1

                        LoadResult.Page(
                            data = domainData,
                            prevKey = if (position == RepositoryConstants.FATSECRET_STARTING_PAGE_INDEX) null else position - 1,
                            nextKey = nextKey
                        )
                    }

                    is ResultWrapper.Error -> {
                        val msg = LogMessages.PAGING_LOAD_API_ERROR.format(
                            apiResult.message, apiResult.code
                        )
                        logWriter.e(TAG, msg)
                        LoadResult.Error(Exception(apiResult.message))
                    }

                    is ResultWrapper.Exception -> {
                        val msg =
                            LogMessages.PAGING_LOAD_API_EXCEPTION.format(apiResult.exception.message)
                        logWriter.e(TAG, msg)
                        LoadResult.Error(apiResult.exception)
                    }
                }
            }

        } catch (e: Exception) {
            val msg = LogMessages.PAGING_LOAD_UNKNOWN_ERROR.format(e.message)
            logWriter.e(TAG, msg, e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
