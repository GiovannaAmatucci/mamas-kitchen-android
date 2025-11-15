package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository
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
    lateinit var repository: FavoriteRepository
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        removeFavoriteUseCase = RemoveFavoriteUseCaseImpl(repository)
    }

    @Test
    fun `RemoveFavoriteUseCase SHOULD call repository removeFavorite with correct id`() = runTest {
        val recipeId = "123"
        coEvery { repository.removeFavorite(recipeId) } just Runs

        removeFavoriteUseCase(recipeId)

        coVerify(exactly = 1) { repository.removeFavorite(recipeId) }
    }


    @Test
    fun `RemoveFavoriteUseCase SHOULD call repository removeFavorite with 'null' string when id is null`() =
        runTest {
            coEvery { repository.removeFavorite("null") } just Runs

            removeFavoriteUseCase(null)

            coVerify(exactly = 1) { repository.removeFavorite("null") }
        }

}