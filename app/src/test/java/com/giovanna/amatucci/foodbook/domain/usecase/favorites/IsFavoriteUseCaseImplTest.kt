package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IsFavoriteUseCaseImplTest {

    @MockK
    lateinit var repository: FavoritesRepository
    private lateinit var isFavoritesUseCase: IsFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isFavoritesUseCase = IsFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `IsFavoriteUseCase SHOULD call repository isFavorite`() = runTest {
        val recipeId = "123"
        val expectedFlow = flowOf(true)
        every { repository.isFavorite(recipeId) } returns expectedFlow

        val resultFlow = isFavoritesUseCase(recipeId)
        val result = resultFlow.first()

        assertEquals(expectedFlow, resultFlow)
        assertTrue(result)
        verify(exactly = 1) { repository.isFavorite(recipeId) }
    }
}