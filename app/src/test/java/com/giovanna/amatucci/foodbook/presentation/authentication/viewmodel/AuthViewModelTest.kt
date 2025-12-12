package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel

import app.cash.turbine.test
import com.giovanna.amatucci.foodbook.MainCoroutineRule
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.UiText
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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

    private val mockTokenResponse =
        TokenResponse(accessToken = "token", tokenType = "Bearer", expiresIn = 3600)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `init SHOULD set Success and navigateToHome WHEN checkAuth returns true`() = runTest {
        coEvery { checkAuthStatusUseCase() } coAnswers { delay(10); true }

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            awaitItem()
            val state = awaitItem()
            assertEquals(ScreenStatus.Success, state.status)
            assertTrue(state.navigateToHome)
        }
    }

    @Test
    fun `init SHOULD fetch token WHEN checkAuth returns false AND succeed`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns false
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Success(mockTokenResponse)

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            if (state.status == ScreenStatus.Success) {
                assertTrue(state.navigateToHome)
            } else {
                val success = awaitItem()
                assertEquals(ScreenStatus.Success, success.status)
                assertTrue(success.navigateToHome)
            }
        }
    }

    @Test
    fun `fetchToken SHOULD set Error state WHEN api returns Error`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns false
        val errorMsg = "Erro 404"
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Error(errorMsg, 404)

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            val errorState = if (state.status !is ScreenStatus.Error) awaitItem() else state

            assertTrue(errorState.status is ScreenStatus.Error)
            assertFalse(errorState.navigateToHome)
            assertEquals(errorMsg, (errorState.error as UiText.DynamicString).value)
        }
    }

    @Test
    fun `fetchToken SHOULD set Error state WHEN api returns Exception with message`() = runTest {
        // ARRANGE
        coEvery { checkAuthStatusUseCase() } returns false
        val exceptionMsg = "Sem internet"
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Exception(Exception(exceptionMsg))

        // ACT
        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        // ASSERT
        viewModel.uiState.test {
            var state = awaitItem()
            while (state.status !is ScreenStatus.Error) {
                state = awaitItem()
            }

            // Agora state é garantidamente um Error (ou o teste falharia no timeout acima)
            assertTrue(
                "Esperado estado de erro, mas foi ${state.status}",
                state.status is ScreenStatus.Error
            )
            assertEquals(exceptionMsg, (state.error as UiText.DynamicString).value)
        }
    }

    @Test
    fun `fetchToken SHOULD set Unknown Error WHEN api returns Exception without message`() =
        runTest {
            // ARRANGE
            coEvery { checkAuthStatusUseCase() } returns false

            // Simula exceção SEM mensagem
            coEvery { fetchTokenUseCase() } returns ResultWrapper.Exception(Exception())

            // ACT
            viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

            // ASSERT
            viewModel.uiState.test {
                var state = awaitItem()

                // Loop para pular Loading
                while (state.status !is ScreenStatus.Error) {
                    state = awaitItem()
                }

                assertTrue(
                    "Esperado estado de erro, mas foi ${state.status}",
                    state.status is ScreenStatus.Error
                )

                // Verifica a conversão para StringResource (Unknown Error)
                val errorText = state.error as? UiText.StringResource
                assertEquals(R.string.auth_error_unknown, errorText?.resId)
            }
        }
    @Test
    fun `onEvent RequestToken SHOULD NOT call usecase if already Loading`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns false
        coEvery { fetchTokenUseCase() } coAnswers {
            delay(500)
            ResultWrapper.Success(mockTokenResponse)
        }

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val item = awaitItem()
            if (item.status !is ScreenStatus.Loading) awaitItem()
        }

        viewModel.onEvent(AuthEvent.RequestToken)

        coVerify(exactly = 1) { fetchTokenUseCase() }
    }

    @Test
    fun `onEvent NavigationCompleted SHOULD reset navigateToHome`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns true
        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            val successState = if (!state.navigateToHome) awaitItem() else state
            assertTrue(successState.navigateToHome)
        }

        viewModel.onEvent(AuthEvent.NavigationCompleted)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.navigateToHome)
        }
    }

    @Test
    fun `onEvent RequestToken SHOULD call fetchToken when not loading`() = runTest {
        coEvery { checkAuthStatusUseCase() } returns false
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Error("Fail", 400)

        viewModel = AuthViewModel(checkAuthStatusUseCase, fetchTokenUseCase)
        viewModel.uiState.test { cancelAndIgnoreRemainingEvents() }
        coEvery { fetchTokenUseCase() } returns ResultWrapper.Success(mockTokenResponse)

        viewModel.onEvent(AuthEvent.RequestToken)

        viewModel.uiState.test {
            val state = awaitItem()
            val finalState = if (state.status is ScreenStatus.Loading) awaitItem() else state
            assertEquals(ScreenStatus.Success, finalState.status)
        }
    }
}