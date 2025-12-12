package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetRecentFavoritesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var repository: FavoritesRepository

    private lateinit var useCase: GetRecentFavoritesUseCase

    private val mockRecipeItem = RecipeItem(
        id = 123L,
        name = "Recent Favorite Burger",
        description = "Delicious",
        imageUrl = "url",
        rating = 5
    )

    @Before
    fun setUp() {
        useCase = GetRecentFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `invoke - WHEN called - THEN returns list flow from repository`() = runTest {
        // ARRANGE
        val expectedList = listOf(mockRecipeItem)
        every { repository.getLastFavorites() } returns flowOf(expectedList)

        // ACT
        val result = useCase().first()

        // ASSERT
        assertEquals(expectedList, result)
        verify(exactly = 1) { repository.getLastFavorites() }
    }
}