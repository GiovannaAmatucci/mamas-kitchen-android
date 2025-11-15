package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel

import app.cash.turbine.test
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.UiText
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthUiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var checkAuthStatusUseCase: CheckAuthenticationStatusUseCase

    @MockK
    lateinit var fetchTokenUseCase: FetchAndSaveTokenUseCase

    private lateinit var viewModel: AuthViewModel

    private val mockTokenResponse = TokenResponse(
        accessToken = "token", tokenType = "Bearer", expiresIn = 3600
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `init SHOULD move to Authenticated WHEN checkAuthStatusUseCase returns true`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns true

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val successState = awaitItem() as AuthUiState.Authenticated
            assertTrue(successState.navigateToHome)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init SHOULD fetch token and move to Authenticated WHEN check returns false and fetch succeeds`() =
        runTest {
            coEvery { checkAuthStatusUseCase() } returns false
            val successResult = ResultWrapper.Success(mockTokenResponse)
            coEvery { fetchTokenUseCase() } returns successResult

            viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

            viewModel.uiState.test {
                val successState = awaitItem() as AuthUiState.Authenticated
                assertTrue(successState.navigateToHome)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init should move to AuthenticationFailed when check returns false and fetch fails (Error)`() =
        runTest {
            coEvery { checkAuthStatusUseCase() } returns false
            val errorResult = ResultWrapper.Error("Not Found", 401)
            coEvery { fetchTokenUseCase() } returns errorResult

            viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

            viewModel.uiState.test {
                val errorState = awaitItem() as AuthUiState.AuthenticationFailed
                assertTrue(errorState.errorMessage is UiText.StringResource)
                assertEquals(
                    R.string.auth_error_with_code,
                    (errorState.errorMessage as UiText.StringResource).resId
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `init SHOULD move to AuthenticationFailed WHEN check returns false and fetch fails (Exception)`() =
        runTest {
            coEvery { checkAuthStatusUseCase() } returns false
            val exception = RuntimeException("Network error")
            val exceptionResult = ResultWrapper.Exception(exception)
            coEvery { fetchTokenUseCase() } returns exceptionResult

            viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

            viewModel.uiState.test {
                val errorState = awaitItem() as AuthUiState.AuthenticationFailed
                assertTrue(errorState.errorMessage is UiText.DynamicString)
                assertEquals(
                    "Network error", (errorState.errorMessage as UiText.DynamicString).value
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onEvent RequestToken SHOULD fetch token and succeed`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns false
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Error(
            "Not Found", 404
        )

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)
        val successResult = ResultWrapper.Success(mockTokenResponse)
        coEvery { fetchTokenUseCase() } returns successResult

        viewModel.uiState.test {
            val initState = awaitItem()
            assertTrue(initState is AuthUiState.AuthenticationFailed)
            viewModel.onEvent(AuthEvent.RequestToken)
            val successState = awaitItem() as AuthUiState.Authenticated
            assertTrue(successState.navigateToHome)
        }
    }

    @Test
    fun `onEvent NavigationCompleted SHOULD update navigateToHome to false`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns true

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val initState = awaitItem() as AuthUiState.Authenticated
            assertTrue(initState.navigateToHome)
            viewModel.onEvent(AuthEvent.NavigationCompleted)
            val finalState = awaitItem() as AuthUiState.Authenticated
            assertFalse(finalState.navigateToHome)
        }
    }
}