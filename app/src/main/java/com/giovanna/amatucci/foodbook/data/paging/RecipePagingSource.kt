package com.giovanna.amatucci.foodbook.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.util.LogMessages
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import timber.log.Timber

private const val FATSECRET_STARTING_PAGE_INDEX = 1

class RecipePagingSource(
    private val api: FatSecretRecipeApi,
    private val mapper: RecipeDataMapper,
    private val query: String,
) : PagingSource<Int, RecipeItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeItem> {
        val position = params.key ?: 1
        val maxResults = params.loadSize

        return try {
            val apiResult = api.searchRecipes(query, position, maxResults)

            when (apiResult) {
                is ResultWrapper.Success -> {
                    val recipesSearch = apiResult.data.recipesSearch
                    val recipeDtos = recipesSearch?.recipeSearch ?: emptyList()

                    if (recipeDtos.isNotEmpty()) {
                        val domainData = recipeDtos.map { mapper.searchRecipeDtoToDomain(it) }

                        LoadResult.Page(
                            data = domainData,
                            prevKey = if (position == FATSECRET_STARTING_PAGE_INDEX) null else position - 1,
                            nextKey = if (domainData.isEmpty()) null else position + 1
                        )
                    } else {
                        LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
                    }
                }

                is ResultWrapper.Error -> {
                    Timber.e(LogMessages.PAGING_SOURCE_LOAD_FAILURE_API, apiResult.code)
                    LoadResult.Error(Exception(LogMessages.PAGING_SOURCE_LOAD_FAILURE_API))
                }

                is ResultWrapper.Exception -> LoadResult.Error(
                    apiResult.exception
                )
            }
        } catch (e: Exception) {
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