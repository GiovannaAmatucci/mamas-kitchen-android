package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
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
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetSearchQueriesUseCaseImplTest {
    @MockK
    lateinit var searchRepository: SearchRepository

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var getSearchQueriesUseCase: GetSearchQueriesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        getSearchQueriesUseCase = GetSearchQueriesUseCaseImpl(searchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GetSearchQueriesUseCase SHOULD call repository getSearchQueries`() = runTest {
        val expectedList = listOf("pizza", "burger")
        coEvery { searchRepository.getSearchQueries() } returns expectedList

        val result = getSearchQueriesUseCase()

        assertEquals(expectedList, result)
        coVerify(exactly = 1) { searchRepository.getSearchQueries() }
    }
}