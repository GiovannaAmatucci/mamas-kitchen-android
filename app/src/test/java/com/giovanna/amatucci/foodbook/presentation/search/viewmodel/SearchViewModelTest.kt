package com.giovanna.amatucci.foodbook.presentation.search.viewmodel

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.domain.usecase.search.ClearSearchHistoryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.GetSearchQueriesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCase
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule() // Usa StandardTestDispatcher

    // Mocks
    @MockK
    lateinit var searchRecipesUseCase: SearchRecipesUseCase

    @MockK
    lateinit var getSearchQueriesUseCase: GetSearchQueriesUseCase

    @MockK
    lateinit var saveSearchQueryUseCase: SaveSearchQueryUseCase

    @MockK
    lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { getSearchQueriesUseCase() } returns listOf("pizza", "burger")
        coEvery { searchRecipesUseCase(any()) } returns flowOf(PagingData.empty())
        coEvery { saveSearchQueryUseCase(any()) } just Runs
        coEvery { clearSearchHistoryUseCase() } just Runs
        viewModel = SearchViewModel(
            searchRecipesUseCase,
            saveSearchQueryUseCase,
            getSearchQueriesUseCase,
            clearSearchHistoryUseCase
        )
    }

    @Test
    fun `init SHOULD load search history`() = runTest {
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { getSearchQueriesUseCase() }
    }

    @Test
    fun `onEvent UpdateSearchQuery should only update searchQuery and not trigger search`() =
        runTest {
            val query = "chicken"
            val job = launch { viewModel.uiState.value.recipes.collect {} }
            viewModel.onEvent(SearchEvent.UpdateSearchQuery(query))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(query, viewModel.uiState.value.searchQuery)
            assertEquals("", viewModel.uiState.value.submittedQuery)
            coVerify(exactly = 0) { searchRecipesUseCase(any()) }

            job.cancel()
        }

    @Test
    fun `onEvent SubmitSearch SHOULD update submittedQuery, save query, AND trigger search`() =
        runTest {
            val query = "pasta"
            val job = launch { viewModel.uiState.value.recipes.collect {} }

            viewModel.onEvent(SearchEvent.UpdateSearchQuery(query))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            viewModel.onEvent(SearchEvent.SubmitSearch(query))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals("pasta", viewModel.uiState.value.searchQuery)
            assertEquals("pasta", viewModel.uiState.value.submittedQuery)
            assertFalse(viewModel.uiState.value.isActive)
            coVerify(exactly = 1) { saveSearchQueryUseCase(query) }
            coVerify(exactly = 1) { searchRecipesUseCase(query) }

            job.cancel()
        }

    @Test
    fun `onEvent SubmitSearch SHOULD do nothing if query is blank`() = runTest {
        val job = launch { viewModel.uiState.value.recipes.collect {} }
        val blankQuery = "   "
        viewModel.onEvent(SearchEvent.UpdateSearchQuery(blankQuery))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(SearchEvent.SubmitSearch(blankQuery))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("", viewModel.uiState.value.submittedQuery)
        coVerify(exactly = 0) { saveSearchQueryUseCase(any()) }
        coVerify(exactly = 0) { searchRecipesUseCase(any()) }
        job.cancel()
    }

    @Test
    fun `onEvent RecentSearchClicked SHOULD update all queries, save, AND trigger search`() =
        runTest {
            val query = "pizza"
            val job = launch { viewModel.uiState.value.recipes.collect {} }
            viewModel.onEvent(SearchEvent.RecentSearchClicked(query))
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            assertEquals("pizza", viewModel.uiState.value.searchQuery)
            assertEquals("pizza", viewModel.uiState.value.submittedQuery)
            assertFalse(viewModel.uiState.value.isActive)
            coVerify(exactly = 1) { saveSearchQueryUseCase(query) }
            coVerify(exactly = 1) { searchRecipesUseCase(query) }
            job.cancel()
        }

    @Test
    fun `onEvent ActiveChanged(true) SHOULD update state AND load history`() = runTest {
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { getSearchQueriesUseCase() }
        viewModel.onEvent(SearchEvent.ActiveChanged(true))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isActive)
        coVerify(exactly = 2) { getSearchQueriesUseCase() }
    }

    @Test
    fun `onEvent ActiveChanged(false) SHOULD only update state`() = runTest {
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { getSearchQueriesUseCase() }
        viewModel.onEvent(SearchEvent.ActiveChanged(false))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isActive)
        coVerify(exactly = 1) { getSearchQueriesUseCase() }
    }

    @Test
    fun `onEvent ClearSearchQuery SHOULD clear searchQuery`() = runTest {
        viewModel.onEvent(SearchEvent.UpdateSearchQuery("testing"))
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("testing", viewModel.uiState.value.searchQuery)
        viewModel.onEvent(SearchEvent.ClearSearchQuery)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle() // Processa o update
        assertEquals("", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onEvent ClearSearchHistory SHOULD call use case`() = runTest {
        viewModel.onEvent(SearchEvent.ClearSearchHistory)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { clearSearchHistoryUseCase() }
    }
}