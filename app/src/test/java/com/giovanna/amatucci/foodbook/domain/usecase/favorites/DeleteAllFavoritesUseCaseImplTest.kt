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

class DeleteAllFavoritesUseCaseImplTest {

    @MockK
    lateinit var repository: FavoritesRepository
    private lateinit var deleteAllFavoritesUseCase: DeleteAllFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteAllFavoritesUseCase = DeleteAllFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `DeleteAllFavoritesUseCase SHOULD call repository deleteAllFavorites`() = runTest {
        coEvery { repository.deleteAllFavorites() } just Runs

        deleteAllFavoritesUseCase()

        coVerify(exactly = 1) { repository.deleteAllFavorites() }
    }

}