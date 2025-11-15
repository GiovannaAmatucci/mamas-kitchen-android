package com.giovanna.amatucci.foodbook.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.UiText
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.AddFavoriteUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.IsFavoriteUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.RemoveFavoriteUseCase
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsStatus
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getRecipeDetailsUseCase: GetRecipeDetailsUseCase

    @MockK
    lateinit var isFavoriteUseCase: IsFavoriteUseCase

    @MockK
    lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @MockK
    lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    private lateinit var viewModel: DetailsViewModel

    private val mockRecipeDetails = mockk<RecipeDetails>(relaxed = true)
    private lateinit var isFavoriteFlow: MutableStateFlow<Boolean>

    private val validRecipeId = "123"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isFavoriteFlow = MutableStateFlow(false)
        every { mockRecipeDetails.id } returns validRecipeId
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Success(
            mockRecipeDetails
        )
        every { isFavoriteUseCase(validRecipeId) } returns isFavoriteFlow // Retorna nosso flow
        coEvery { addFavoriteUseCase(any()) } just Runs
        coEvery { removeFavoriteUseCase(any()) } just Runs
    }

    private fun createViewModel(recipeId: String): DetailsViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("recipeId" to recipeId))
        return DetailsViewModel(
            getRecipeDetailsUseCase,
            isFavoriteUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `init SHOULD fetch details and observe favorite status WHEN ID is valid`() = runTest {
        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val successState = awaitItem()

            assertEquals(DetailsStatus.Success, successState.status)
            assertEquals(mockRecipeDetails, successState.recipe)
            assertFalse(successState.isFavorite == true)
        }
        coVerify(exactly = 1) { getRecipeDetailsUseCase(validRecipeId) }
        coVerify(exactly = 1) { isFavoriteUseCase(validRecipeId) }
    }

    @Test
    fun `init SHOULD set error state WHEN ID is blank`() = runTest {
        viewModel = createViewModel("") // ID inválido

        viewModel.uiState.test {
            val errorState = awaitItem()

            assertEquals(DetailsStatus.Error, errorState.status)
            val errorMessage = errorState.error as UiText.StringResource
            assertEquals(R.string.details_error_invalid_id, errorMessage.resId)
        }

        coVerify(exactly = 0) { getRecipeDetailsUseCase(any()) }
    }

    @Test
    fun `init SHOULD set error state WHEN getRecipeDetails fails`() = runTest {
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Error(
            "Not Found", 404
        )

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val errorState = awaitItem()

            assertEquals(DetailsStatus.Error, errorState.status)
            val errorMessage = errorState.error as UiText.StringResource
            assertEquals(R.string.details_error_api_failure, errorMessage.resId)
        }
    }

    @Test
    fun `event ToggleFavorite SHOULD call addFavorite and update state WHEN not favorite`() =
        runTest {
            isFavoriteFlow.value = false
            viewModel = createViewModel(validRecipeId)

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertFalse(initialState.isFavorite == true)
                viewModel.onEvent(DetailsEvent.ToggleFavorite)
                coVerify(exactly = 1) { addFavoriteUseCase(mockRecipeDetails) }
                coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
                isFavoriteFlow.value = true
                val updatedState = awaitItem()
                assertTrue(updatedState.isFavorite == true)
            }
        }

    @Test
    fun `event ToggleFavorite SHOULD call removeFavorite and update state WHEN favorite`() =
        runTest {
            isFavoriteFlow.value = true
            viewModel = createViewModel(validRecipeId)

            viewModel.uiState.test {
                val initialState = awaitItem()
                assertTrue(initialState.isFavorite == true)
                viewModel.onEvent(DetailsEvent.ToggleFavorite)
                coVerify(exactly = 0) { addFavoriteUseCase(any()) }
                coVerify(exactly = 1) { removeFavoriteUseCase(validRecipeId) }
                isFavoriteFlow.value = false
                val updatedState = awaitItem()
                assertFalse(updatedState.isFavorite == true)
            }
        }

    @Test
    fun `event ToggleFavorite SHOULD do nothing if recipe is null`() = runTest {
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Error("Error", 404)

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(DetailsStatus.Error, initialState.status)
            assertEquals(null, initialState.recipe)
            viewModel.onEvent(DetailsEvent.ToggleFavorite)
            coVerify(exactly = 0) { addFavoriteUseCase(any()) }
            coVerify(exactly = 0) { removeFavoriteUseCase(any()) }
            expectNoEvents()
        }
    }
}