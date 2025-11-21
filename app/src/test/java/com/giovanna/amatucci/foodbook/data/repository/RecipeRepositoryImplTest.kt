package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.remote.api.FatSecretRecipeApi
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Recipe
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeResponse
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
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
class RecipeRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var api: FatSecretRecipeApi

    @MockK
    private lateinit var mapper: RecipeDataMapper

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var repository: RecipeRepositoryImpl
    private val mockRecipeDto = Recipe(
        recipeId = "123",
        recipeName = "Test",
        recipeDescription = "Test Description",
        recipeImages = null,
        recipeTypes = null,
        recipeCategories = null,
        recipeUrl = null,
        numberOfServings = "4",
        preparationTimeMin = "10 min",
        cookingTimeMin = "20 min",
    )
    private val mockApiResponse = RecipeResponse(recipe = mockRecipeDto)
    private val mockApiSuccessResult = ResultWrapper.Success(mockApiResponse)
    private val mockRecipeDetails = RecipeDetails(
        id = "123",
        name = "Test Domain",
        description = "Test Description",
        imageUrls = listOf("http://example.com/img.jpg"),
        preparationTime = "10 min",
        cookingTime = "20 min",
        servings = "4",
        ingredients = emptyList(),
        directions = emptyList(),
        categories = emptyList()
    )

    private val testRecipeId = "123"

    @Before
    fun setUp() {
        repository = RecipeRepositoryImpl(api, mapper, logWriter)
        every { mapper.recipeDetailDtoToDomain(mockRecipeDto) } returns mockRecipeDetails
    }

    @Test
    fun `getRecipeDetails - GIVEN API and Mapper SUCCESS - THEN returns ResultWrapper Success`() =
        runTest {
            // ARRANGE
            coEvery { api.getRecipeDetails(testRecipeId) } returns mockApiSuccessResult
            every { mapper.recipeDetailDtoToDomain(mockRecipeDto) } returns mockRecipeDetails

            // ACT
            val result = repository.getRecipeDetails(testRecipeId)

            // ASSERT
            assertTrue(result is ResultWrapper.Success)
            assertEquals(mockRecipeDetails, (result as ResultWrapper.Success).data)
            coVerify(exactly = 1) { api.getRecipeDetails(testRecipeId) }
            verify(exactly = 1) { mapper.recipeDetailDtoToDomain(mockRecipeDto) }
            verify(exactly = 2) { logWriter.d(eq("RecipeRepository"), any()) }
            verify(exactly = 0) { logWriter.e(any(), any(), any()) }
        }

    @Test
    fun `getRecipeDetails - GIVEN API returns Error - THEN returns ResultWrapper Error`() =
        runTest {
            // ARRANGE
            val apiError = ResultWrapper.Error("Not Found", 404)
            coEvery { api.getRecipeDetails(testRecipeId) } returns apiError

            // ACT
            val result = repository.getRecipeDetails(testRecipeId)

            // ASSERT
            assertTrue(result is ResultWrapper.Error)
            assertEquals(404, (result as ResultWrapper.Error).code)
            verify(exactly = 0) { mapper.recipeDetailDtoToDomain(any()) }
            verify(exactly = 1) { logWriter.e(eq("RecipeRepository"), any(), null) }
        }

    @Test
    fun `getRecipeDetails - GIVEN API returns Exception - THEN returns ResultWrapper Exception`() =
        runTest {
            // ARRANGE
            val apiException = IOException("Network down")
            val apiExceptionResult = ResultWrapper.Exception(apiException)
            coEvery { api.getRecipeDetails(testRecipeId) } returns apiExceptionResult

            // ACT
            val result = repository.getRecipeDetails(testRecipeId)

            // ASSERT
            assertTrue(result is ResultWrapper.Exception)
            assertEquals(apiException, (result as ResultWrapper.Exception).exception)
            verify(exactly = 0) { mapper.recipeDetailDtoToDomain(any()) }
            verify(exactly = 1) { logWriter.e(eq("RecipeRepository"), any(), null) }
        }

    @Test
    fun `getRecipeDetails - Given API Success BUT Mapper throws Exception - Then returns ResultWrapper Exception`() =
        runTest {
            // ARRANGE
            val mapperException = NullPointerException("DTO field was null")
            coEvery { api.getRecipeDetails(testRecipeId) } returns mockApiSuccessResult
            every { mapper.recipeDetailDtoToDomain(mockRecipeDto) } throws mapperException

            // ACT
            val result = repository.getRecipeDetails(testRecipeId)

            // ASSERT
            assertTrue(result is ResultWrapper.Exception)
            assertEquals(mapperException, (result as ResultWrapper.Exception).exception)
            coVerify(exactly = 1) { api.getRecipeDetails(testRecipeId) }
            verify(exactly = 1) { mapper.recipeDetailDtoToDomain(mockRecipeDto) }
            verify(exactly = 1) { logWriter.e(eq("RecipeRepository"), any(), null) }
        }
}