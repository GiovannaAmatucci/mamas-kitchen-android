package com.giovanna.amatucci.foodbook.domain.usecase.auth

import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckAuthenticationStatusUseCaseTest {
    @MockK
    lateinit var tokenRepository: TokenRepository
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var checkAuthUseCase: CheckAuthenticationStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        checkAuthUseCase = CheckAuthenticationStatusUseCaseImpl(tokenRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `CheckAuthenticationStatusUseCase SHOULD return true WHEN token is valid`() = runTest {
        coEvery { tokenRepository.getValidAccessToken() } returns "a_valid_token"

        val result = checkAuthUseCase()

        assertTrue(result)
        coVerify(exactly = 1) { tokenRepository.getValidAccessToken() }
    }

    @Test
    fun `CheckAuthenticationStatusUseCase SHOULD return false WHEN token is null`() = runTest {
        coEvery { tokenRepository.getValidAccessToken() } returns null

        val result = checkAuthUseCase()

        assertFalse(result)
        coVerify(exactly = 1) { tokenRepository.getValidAccessToken() }
    }
}