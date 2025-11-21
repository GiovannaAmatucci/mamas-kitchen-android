package com.giovanna.amatucci.foodbook.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.AddFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.IsFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.RemoveFavoritesUseCase
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsStatus
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.UiConstants
import com.giovanna.amatucci.foodbook.util.constants.UiText
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
    lateinit var getFavoritesDetailsUseCase: GetFavoritesDetailsUseCase

    @MockK
    lateinit var isFavoritesUseCase: IsFavoritesUseCase

    @MockK
    lateinit var addFavoritesUseCase: AddFavoritesUseCase

    @MockK
    lateinit var removeFavoritesUseCase: RemoveFavoritesUseCase

    private lateinit var viewModel: DetailsViewModel
    private val mockRecipeDetails = mockk<RecipeDetails>(relaxed = true)
    private lateinit var isFavoriteFlow: MutableStateFlow<Boolean>
    private val validRecipeId = "123"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        isFavoriteFlow = MutableStateFlow(false)
        every { mockRecipeDetails.id } returns validRecipeId
        every { isFavoritesUseCase(validRecipeId) } returns isFavoriteFlow
        coEvery { addFavoritesUseCase(any()) } just Runs
        coEvery { removeFavoritesUseCase(any()) } just Runs
    }

    private fun createViewModel(recipeId: String?): DetailsViewModel {
        val map =
            if (recipeId != null) mapOf(UiConstants.DETAILS_VIEW_MODEL_RECIPE_ID to recipeId) else emptyMap()
        val savedStateHandle = SavedStateHandle(map)

        return DetailsViewModel(
            getRecipeDetailsUseCase,
            isFavoritesUseCase,
            getFavoritesDetailsUseCase,
            addFavoritesUseCase,
            removeFavoritesUseCase,
            savedStateHandle
        )
    }

    @Test
    fun `init SHOULD set Error WHEN recipeId is null or blank`() = runTest {
        viewModel = createViewModel("")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(DetailsStatus.Error, state.status)
            assertEquals(
                R.string.details_error_invalid_id, (state.error as UiText.StringResource).resId
            )
        }
    }

    @Test
    fun `init SHOULD fetch from CACHE and skip API`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns mockRecipeDetails

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(DetailsStatus.Success, state.status)
            assertEquals(mockRecipeDetails, state.recipe)
        }
        coVerify(exactly = 0) { getRecipeDetailsUseCase(any()) }
    }

    @Test
    fun `init SHOULD fetch from API WHEN cache is null`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns null
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Success(
            mockRecipeDetails
        )

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val state = awaitItem()
            val successState = if (state.status == DetailsStatus.Loading) awaitItem() else state
            assertEquals(DetailsStatus.Success, successState.status)
            assertEquals(mockRecipeDetails, successState.recipe)
        }
    }

    @Test
    fun `init SHOULD set Error WHEN api fails`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns null
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Error("Erro", 500)

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val state = awaitItem()
            val errorState = if (state.status != DetailsStatus.Error) awaitItem() else state
            assertEquals(DetailsStatus.Error, errorState.status)
        }
    }

    @Test
    fun `init SHOULD set Error WHEN api throws exception`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns null
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Exception(Exception())

        viewModel = createViewModel(validRecipeId)

        viewModel.uiState.test {
            val state = awaitItem()
            val errorState = if (state.status != DetailsStatus.Error) awaitItem() else state
            assertEquals(DetailsStatus.Error, errorState.status)
        }
    }

    @Test
    fun `toggleFavorite SHOULD add favorite WHEN not currently favorite`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns mockRecipeDetails
        isFavoriteFlow.value = false

        viewModel = createViewModel(validRecipeId)
        viewModel.uiState.test { cancelAndIgnoreRemainingEvents() }

        viewModel.onEvent(DetailsEvent.ToggleFavorite)

        coVerify { addFavoritesUseCase(mockRecipeDetails) }
        coVerify(exactly = 0) { removeFavoritesUseCase(any()) }
    }

    @Test
    fun `toggleFavorite SHOULD remove favorite WHEN currently favorite`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns mockRecipeDetails
        isFavoriteFlow.value = true

        viewModel = createViewModel(validRecipeId)
        viewModel.uiState.test { cancelAndIgnoreRemainingEvents() }

        viewModel.onEvent(DetailsEvent.ToggleFavorite)

        coVerify { removeFavoritesUseCase(validRecipeId) }
        coVerify(exactly = 0) { addFavoritesUseCase(any()) }
    }

    @Test
    fun `toggleFavorite SHOULD do nothing WHEN recipe is null (Guard Clause)`() = runTest {
        viewModel = createViewModel("")

        viewModel.onEvent(DetailsEvent.ToggleFavorite)

        coVerify(exactly = 0) { addFavoritesUseCase(any()) }
        coVerify(exactly = 0) { removeFavoritesUseCase(any()) }
    }

    @Test
    fun `toggleFavorite SHOULD do nothing WHEN recipeId is blank (Guard Clause)`() = runTest {
        val invalidRecipe = mockk<RecipeDetails>(relaxed = true)
        every { invalidRecipe.id } returns ""

        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns invalidRecipe

        viewModel = createViewModel(validRecipeId)
        viewModel.uiState.test { cancelAndIgnoreRemainingEvents() }

        viewModel.onEvent(DetailsEvent.ToggleFavorite)

        coVerify(exactly = 0) { addFavoritesUseCase(any()) }
        coVerify(exactly = 0) { removeFavoritesUseCase(any()) }
    }

    @Test
    fun `onEvent RetryConnection SHOULD retry fetching details`() = runTest {
        coEvery { getFavoritesDetailsUseCase(validRecipeId) } returns null
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Error("Fail", 500)

        viewModel = createViewModel(validRecipeId)
        viewModel.uiState.test { awaitItem(); cancelAndIgnoreRemainingEvents() }
        coEvery { getRecipeDetailsUseCase(validRecipeId) } returns ResultWrapper.Success(
            mockRecipeDetails
        )

        viewModel.onEvent(DetailsEvent.RetryConnection)

        viewModel.uiState.test {
            val state = awaitItem()
            val successState = if (state.status != DetailsStatus.Success) awaitItem() else state
            assertEquals(DetailsStatus.Success, successState.status)
        }
        coVerify(exactly = 2) { getRecipeDetailsUseCase(validRecipeId) }
    }

    @Test
    fun `onEvent RetryConnection SHOULD do nothing if recipeId is null`() = runTest {
        viewModel = createViewModel(null)

        viewModel.onEvent(DetailsEvent.RetryConnection)
        coVerify(exactly = 0) { getRecipeDetailsUseCase(any()) }
    }
}