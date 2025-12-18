package com.giovanna.amatucci.foodbook.domain.usecase.auth

import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.util.ResultWrapper
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
class FetchAndSaveTokenUseCaseImplTest {
    @MockK
    lateinit var authRepository: AuthRepository
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var fetchTokenUseCase: FetchAndSaveTokenUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        fetchTokenUseCase = FetchAndSaveTokenUseCaseImpl(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `FetchAndSaveTokenUseCase SHOULD call repository and RETURN its result`() = runTest {
        val mockToken = TokenResponse(accessToken = "token", expiresIn = 3600)
        val expectedResult = ResultWrapper.Success(mockToken)
        coEvery { authRepository.fetchAndSaveToken() } returns expectedResult

        val result = fetchTokenUseCase()

        assertEquals(expectedResult, result)
        coVerify(exactly = 1) { authRepository.fetchAndSaveToken() }
    }
}