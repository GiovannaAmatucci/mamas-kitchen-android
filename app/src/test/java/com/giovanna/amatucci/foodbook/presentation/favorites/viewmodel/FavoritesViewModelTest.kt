package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.DeleteAllFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesUseCase
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @MockK
    lateinit var deleteAllFavoritesUseCase: DeleteAllFavoritesUseCase

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { getFavoritesUseCase(any()) } returns flowOf(PagingData.empty())

        coEvery { deleteAllFavoritesUseCase() } just Runs

        viewModel = FavoritesViewModel(getFavoritesUseCase, deleteAllFavoritesUseCase)
    }

    @Test
    fun `init SHOULD trigger getFavoritesUseCase with empty query after debounce`() = runTest {
        assertNotNull(viewModel.uiState.value.recipes)
        verify(exactly = 0) { getFavoritesUseCase(any()) }
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        job.cancel()
    }

    @Test
    fun `onEvent UpdateSearchQuery SHOULD update state and trigger use case AFTER debounce`() =
        runTest {
            val newQuery = "chicken"
            val job = launch { viewModel.uiState.value.recipes.collect {} }
            mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
            verify(exactly = 1) { getFavoritesUseCase("") }
            viewModel.onEvent(FavoritesEvent.UpdateSearchQuery(newQuery))
            assertEquals(newQuery, viewModel.uiState.value.searchQuery)
            mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
            verify(exactly = 1) { getFavoritesUseCase(newQuery) }

            job.cancel()
        }

    @Test
    fun `onEvent SubmitSearch SHOULD update state and trigger use case AFTER debounce`() = runTest {
        val newQuery = "pasta"

        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        viewModel.onEvent(FavoritesEvent.SubmitSearch(newQuery))
        assertEquals(newQuery, viewModel.uiState.value.searchQuery)

        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase(newQuery) }
        job.cancel()
    }

    @Test
    fun `onEvent UpdateSearchQuery SHOULD not trigger use case if query is unchanged`() = runTest {
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        viewModel.onEvent(FavoritesEvent.UpdateSearchQuery(""))
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        job.cancel()
    }

    @Test
    fun `onEvent ClearSearchQuery SHOULD trigger use case with empty query`() = runTest {
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        viewModel.onEvent(FavoritesEvent.UpdateSearchQuery("beef"))
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("beef") }
        viewModel.onEvent(FavoritesEvent.ClearSearchQuery)
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 2) { getFavoritesUseCase("") }
        job.cancel()
    }

    @Test
    fun `onEvent ShowDeleteAllConfirmation SHOULD update dialog state to true`() {
        viewModel.onEvent(FavoritesEvent.ShowDeleteAllConfirmation)
        assertTrue(viewModel.uiState.value.showConfirmDeleteAllDialog)
    }

    @Test
    fun `onEvent DismissDeleteAllConfirmation SHOULD update dialog state to false`() {
        viewModel.onEvent(FavoritesEvent.ShowDeleteAllConfirmation)
        assertTrue(viewModel.uiState.value.showConfirmDeleteAllDialog)

        viewModel.onEvent(FavoritesEvent.DismissDeleteAllConfirmation)
        assertFalse(viewModel.uiState.value.showConfirmDeleteAllDialog)
    }

    @Test
    fun `onEvent ConfirmDeleteAll SHOULD call use case and dismiss dialog`() = runTest {
        viewModel.onEvent(FavoritesEvent.ShowDeleteAllConfirmation)
        assertTrue(viewModel.uiState.value.showConfirmDeleteAllDialog)
        viewModel.onEvent(FavoritesEvent.ConfirmDeleteAll)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { deleteAllFavoritesUseCase() }
        assertFalse(viewModel.uiState.value.showConfirmDeleteAllDialog)
    }

    @Test
    fun `onEvent SubmitSearch SHOULD not trigger use case if query unchanged`() = runTest {
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        viewModel.onEvent(FavoritesEvent.SubmitSearch(""))
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }

        job.cancel()
    }

    @Test
    fun `onEvent ClearSearchQuery SHOULD not trigger use case if already empty`() = runTest {
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }
        viewModel.onEvent(FavoritesEvent.ClearSearchQuery)
        mainCoroutineRule.testDispatcher.scheduler.advanceTimeBy(301L)
        verify(exactly = 1) { getFavoritesUseCase("") }

        job.cancel()
    }
}