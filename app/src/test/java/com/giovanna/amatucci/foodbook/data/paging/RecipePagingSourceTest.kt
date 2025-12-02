package com.giovanna.amatucci.foodbook.data.paging

import androidx.paging.PagingSource
import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipeSearch
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipesSearch
import com.giovanna.amatucci.foodbook.data.remote.model.search.SearchResponse
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class RecipePagingSourceTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var api: FatSecretRecipeApi

    @MockK
    private lateinit var mapper: RecipeDataMapper

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var pagingSource: RecipePagingSource

    private val testQuery = "chicken"
    private val testLoadSize = 20

    private val mockRecipeSearchDto = RecipeSearch(
        recipeId = "123",
        recipeName = "Chicken Test",
        recipeDescription = "A DTO for chicken",
        recipeImage = "http://image.com/123.jpg",
        recipeIngredientsSearch = null,
        recipeNutritionSearch = null,
        recipeTypesSearch = null
    )
    private val mockRecipeItem = RecipeItem(
        id = 123L,
        name = "Chicken Test",
        description = "A DTO for chicken",
        imageUrl = "http://image.com/123.jpg"
    )
    private val mockApiSuccessResponse = ResultWrapper.Success(
        SearchResponse(
            recipesSearch = RecipesSearch(
                recipeSearch = listOf(mockRecipeSearchDto),
                maxResults = "10",
                pageNumber = "1",
                totalResults = "1"
            )
        )
    )
    private val mockApiEmptyResponse = ResultWrapper.Success(
        SearchResponse(
            recipesSearch = RecipesSearch(
                recipeSearch = emptyList(), maxResults = "10", pageNumber = "1", totalResults = "0"

            )
        )
    )
    private val mockApiNullsResponse = ResultWrapper.Success(
        SearchResponse(
            recipesSearch = null
        )
    )

    @Before
    fun setUp() {
        every { mapper.searchRecipeDtoToDomain(mockRecipeSearchDto) } returns mockRecipeItem
        pagingSource = RecipePagingSource(api, mapper, testQuery, logWriter)
    }

    @Test
    fun `load - GIVEN first load (Refresh) SUCCESS - THEN returns LoadResult Page`() = runTest {
        // GIVEN
        val params = PagingSource.LoadParams.Refresh<Int>(
            key = null, loadSize = testLoadSize, placeholdersEnabled = false
        )
        coEvery {
            api.searchRecipes(
                testQuery,
                RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX,
                testLoadSize
            )
        } returns mockApiSuccessResponse

        // WHEN
        val result = pagingSource.load(params)

        // THEN
        val expected = PagingSource.LoadResult.Page(
            data = listOf(mockRecipeItem),
            prevKey = null,
            nextKey = RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX + 1
        )

        assertEquals(expected, result)
        coVerify(exactly = 1) {
            api.searchRecipes(
                testQuery,
                RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX,
                testLoadSize
            )
        }
        verify(exactly = 1) { mapper.searchRecipeDtoToDomain(mockRecipeSearchDto) }
    }

    @Test
    fun `load - GIVEN next page load (Append) SUCCESS - THEN returns LoadResult Page`() = runTest {
        // GIVEN
        val currentPage = 3
        val params = PagingSource.LoadParams.Append<Int>(
            key = currentPage, loadSize = testLoadSize, placeholdersEnabled = false
        )
        coEvery {
            api.searchRecipes(testQuery, currentPage, testLoadSize)
        } returns mockApiSuccessResponse

        // WHEN
        val result = pagingSource.load(params)

        // THEN
        val expected = PagingSource.LoadResult.Page(
            data = listOf(mockRecipeItem), prevKey = currentPage - 1, nextKey = currentPage + 1
        )

        assertEquals(expected, result)
    }

    @Test
    fun `load - GIVEN empty response (end of list) - THEN returns Page with nextKey null`() =
        runTest {
            // GIVEN
            val currentPage = 5
            val params = PagingSource.LoadParams.Append(
                key = currentPage, loadSize = testLoadSize, placeholdersEnabled = false
            )
            coEvery {
                api.searchRecipes(testQuery, currentPage, testLoadSize)
            } returns mockApiEmptyResponse

            // WHEN
            val result = pagingSource.load(params)

            // THEN
            val expected = PagingSource.LoadResult.Page(
                data = emptyList(), prevKey = currentPage - 1, nextKey = null
            )

            assertEquals(expected, result)
            verify(exactly = 0) { mapper.searchRecipeDtoToDomain(any()) }
        }

    @Test
    fun `load - GIVEN response with 'recipesSearch' null (Robustness) - THEN returns Empty Page`() =
        runTest {
            // GIVEN
            val params = PagingSource.LoadParams.Refresh<Int>(
                key = null, loadSize = testLoadSize, placeholdersEnabled = false
            )
            // CORREÇÃO 2 e 3
            coEvery {
                api.searchRecipes(
                    testQuery,
                    RepositoryConstants.RECIPE_PAGING_SOURCE_STARTING_PAGE_INDEX,
                    testLoadSize
                )
            } returns mockApiNullsResponse

            // WHEN
            val result = pagingSource.load(params)

            // THEN
            val expected = PagingSource.LoadResult.Page(
                data = emptyList(), prevKey = null, nextKey = null
            )

            assertEquals(expected, result)
            verify(exactly = 0) { mapper.searchRecipeDtoToDomain(any()) }
        }


    @Test
    fun `load - GIVEN api returns a ResultWrapper Error - THEN returns a LoadResult Error`() =
        runTest {
            // GIVEN
            val params = PagingSource.LoadParams.Refresh<Int>(
                key = null, loadSize = testLoadSize, placeholdersEnabled = false
            )
            val apiError = ResultWrapper.Error(code = 404, message = "Not Found")
            coEvery { api.searchRecipes(any(), any(), any()) } returns apiError

            // WHEN
            val result = pagingSource.load(params)

            // THEN
            assertTrue(result is PagingSource.LoadResult.Error)
            assertEquals("Not Found", (result as PagingSource.LoadResult.Error).throwable.message)

            verify { logWriter.e("RecipePagingSource", any(), null) }
        }

    @Test
    fun `load - GIVEN api returns ResultWrapper Exception - THEN returns LoadResult Error`() =
        runTest {
            // GIVEN
            val params = PagingSource.LoadParams.Refresh<Int>(
                key = null, loadSize = testLoadSize, placeholdersEnabled = false
            )
            val exception = IOException("Network Error")
            val apiException = ResultWrapper.Exception(exception)
            coEvery { api.searchRecipes(any(), any(), any()) } returns apiException

            // WHEN
            val result = pagingSource.load(params)

            // THEN
            assertTrue(result is PagingSource.LoadResult.Error)
            assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)

            verify { logWriter.e("RecipePagingSource", any(), null) }
        }

    @Test
    fun `load - GIVEN API call throws Unknown Exception - THEN returns LoadResult Error`() =
        runTest {
            // GIVEN
            val params = PagingSource.LoadParams.Refresh<Int>(
                key = null, loadSize = testLoadSize, placeholdersEnabled = false
            )
            val exception = RuntimeException("Crash inesperado")
            coEvery { api.searchRecipes(any(), any(), any()) } throws exception

            // WHEN
            val result = pagingSource.load(params)

            // THEN
            assertTrue(result is PagingSource.LoadResult.Error)
            assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)

            verify { logWriter.e("RecipePagingSource", any(), exception) }
        }
}