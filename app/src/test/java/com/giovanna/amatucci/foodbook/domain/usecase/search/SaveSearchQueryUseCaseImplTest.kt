package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveSearchQueryUseCaseImplTest {

    @MockK
    lateinit var searchRepository: SearchRepository

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var saveSearchQueryUseCase: SaveSearchQueryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        saveSearchQueryUseCase = SaveSearchQueryUseCaseImpl(searchRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SaveSearchQueryUseCase SHOULD call repository saveSearchQuery`() = runTest {
        val query = "pasta"
        coEvery { searchRepository.saveSearchQuery(query) } just Runs

        saveSearchQueryUseCase(query)

        coVerify(exactly = 1) { searchRepository.saveSearchQuery(query) }
    }

}