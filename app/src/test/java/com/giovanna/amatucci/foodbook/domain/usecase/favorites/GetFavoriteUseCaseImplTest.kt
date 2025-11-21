package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoriteUseCaseImplTest {
    @MockK
    lateinit var repository: FavoritesRepository
    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFavoritesUseCase = GetFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `GetFavoritesUseCase SHOULD call repository getFavorites`() = runTest {
        val query = "test"
        val pagingData = PagingData.empty<RecipeItem>()
        val expectedFlow = flowOf(pagingData)
        every { repository.getFavorites(query) } returns expectedFlow

        val resultFlow = getFavoritesUseCase(query)

        assertEquals(expectedFlow, resultFlow)
        verify(exactly = 1) { repository.getFavorites(query) }
    }
}