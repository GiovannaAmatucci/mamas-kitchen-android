package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoritesDetailsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var repository: FavoritesRepository

    private lateinit var useCase: GetFavoritesDetailsUseCase
    private val mockRecipeDetails = mockk<RecipeDetails>()
    private val testRecipeId = "123"

    @Before
    fun setUp() {
        useCase = GetFavoritesDetailsUseCaseImpl(repository)
    }

    @Test
    fun `invoke - GIVEN valid id - THEN calls repository and returns details`() = runTest {
        // ARRANGE
        coEvery { repository.getFavoriteDetails(testRecipeId) } returns mockRecipeDetails

        // ACT
        val result = useCase(testRecipeId)

        // ASSERT
        assertEquals(mockRecipeDetails, result)
        coVerify(exactly = 1) { repository.getFavoriteDetails(testRecipeId) }
    }

    @Test
    fun `invoke - GIVEN id not found - THEN returns null`() = runTest {
        // ARRANGE
        coEvery { repository.getFavoriteDetails(testRecipeId) } returns null

        // ACT
        val result = useCase(testRecipeId)

        // ASSERT
        assertNull(result)
        coVerify(exactly = 1) { repository.getFavoriteDetails(testRecipeId) }
    }
}