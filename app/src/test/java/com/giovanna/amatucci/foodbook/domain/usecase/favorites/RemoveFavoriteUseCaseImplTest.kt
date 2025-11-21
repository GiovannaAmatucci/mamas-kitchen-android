package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveFavoriteUseCaseImplTest {
    @MockK
    lateinit var repository: FavoritesRepository
    private lateinit var removeFavoritesUseCase: RemoveFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        removeFavoritesUseCase = RemoveFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `RemoveFavoriteUseCase SHOULD call repository removeFavorite with correct id`() = runTest {
        val recipeId = "123"
        coEvery { repository.removeFavorite(recipeId) } just Runs

        removeFavoritesUseCase(recipeId)

        coVerify(exactly = 1) { repository.removeFavorite(recipeId) }
    }


    @Test
    fun `RemoveFavoriteUseCase SHOULD call repository removeFavorite with 'null' string when id is null`() =
        runTest {
            coEvery { repository.removeFavorite("null") } just Runs

            removeFavoritesUseCase(null)

            coVerify(exactly = 1) { repository.removeFavorite("null") }
        }

}