package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthState
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val checkAuthStatusUseCase: CheckAuthenticationStatusUseCase,
    private val fetchTokenUseCase: FetchAndSaveTokenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    init {
        checkExistingToken()
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.RequestToken -> fetchToken()
            AuthEvent.NavigationCompleted -> onNavigationHandled()
        }
    }

    private fun checkExistingToken() {
        _uiState.update { it.copy(status = ScreenStatus.Loading) }
        verifyToken()
    }

    private fun verifyToken() {
        viewModelScope.launch {
            runCatching { checkAuthStatusUseCase() }.onSuccess { result ->
                result.takeIf { it }?.let {
                    _uiState.update {
                        it.copy(status = ScreenStatus.Success, navigateToHome = true)
                    }
                } ?: executeTokenFetch()
            }.onFailure { handleErrorException(error = it) }
        }
    }

    private fun fetchToken() {
        if (_uiState.value.status is ScreenStatus.Loading) return
        _uiState.update { it.copy(status = ScreenStatus.Loading) }
        executeTokenFetch()
    }

    private fun executeTokenFetch() {
        viewModelScope.launch {
            runCatching { fetchTokenUseCase.invoke() }.onSuccess { result ->
                handleTokenResult(result = result)
            }.onFailure { e -> handleErrorException(error = e) }
        }
    }

    private fun handleTokenResult(result: ResultWrapper<TokenResponse>) {
        when (result) {
            is ResultWrapper.Success -> {
                _uiState.update {
                    it.copy(status = ScreenStatus.Success, navigateToHome = true)
                }
            }

            is ResultWrapper.Error -> {
                _uiState.update {
                    it.copy(
                        status = ScreenStatus.Error(UiText.StringResource(R.string.auth_error_with_code)),
                        navigateToHome = false,
                        error = UiText.DynamicString(result.message)
                    )
                }
            }

            is ResultWrapper.Exception -> {
                handleErrorException(error = result.exception)
            }
        }
    }

    private fun handleErrorException(error: Throwable) {
        val errorMessage = error.message?.let {
            UiText.DynamicString(it)
        } ?: UiText.StringResource(R.string.auth_error_unknown)

        _uiState.update {
            it.copy(
                status = ScreenStatus.Error(errorMessage),
                navigateToHome = false,
                error = errorMessage
            )
        }
    }

    private fun onNavigationHandled() {
        _uiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}