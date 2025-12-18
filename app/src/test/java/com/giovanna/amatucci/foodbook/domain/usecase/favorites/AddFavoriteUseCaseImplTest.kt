package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddFavoriteUseCaseImplTest {

    @MockK
    lateinit var repository: FavoritesRepository
    private lateinit var addFavoritesUseCase: AddFavoritesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addFavoritesUseCase = AddFavoritesUseCaseImpl(repository)
    }

    @Test
    fun `AddFavoriteUseCase SHOULD call repository addFavorite`() = runTest {
        val mockRecipe = mockk<RecipeDetails>()
        coEvery { repository.addFavorite(mockRecipe) } just Runs

        addFavoritesUseCase(mockRecipe)

        coVerify(exactly = 1) { repository.addFavorite(mockRecipe) }
    }
}