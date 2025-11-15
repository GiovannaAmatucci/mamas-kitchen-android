package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository
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
    lateinit var repository: FavoriteRepository
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addFavoriteUseCase = AddFavoriteUseCaseImpl(repository)
    }

    @Test
    fun `AddFavoriteUseCase SHOULD call repository addFavorite`() = runTest {
        val mockRecipe = mockk<RecipeDetails>()
        coEvery { repository.addFavorite(mockRecipe) } just Runs

        addFavoriteUseCase(mockRecipe)

        coVerify(exactly = 1) { repository.addFavorite(mockRecipe) }
    }
}