package com.giovanna.amatucci.foodbook.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.UiText
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(
    private val checkAuthStatusUseCase: CheckAuthenticationStatusUseCase,
    private val fetchTokenUseCase: FetchAndSaveTokenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
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

    private fun checkExistingToken() = viewModelScope.launch {
        _uiState.value = AuthUiState.Loading
        if (checkAuthStatusUseCase.invoke()) {
            _uiState.value = AuthUiState.Authenticated(navigateToHome = true)
        } else {
            executeTokenFetch()
        }
    }
    private fun fetchToken() = viewModelScope.launch {
        if (_uiState.value is AuthUiState.Loading) return@launch

        _uiState.value = AuthUiState.Loading
        executeTokenFetch()
    }

    private suspend fun executeTokenFetch() {

        fetchTokenUseCase.invoke().let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.value = AuthUiState.Authenticated(navigateToHome = true)
                }

                is ResultWrapper.Error -> {
                    _uiState.value = AuthUiState.AuthenticationFailed(
                        UiText.StringResource(R.string.auth_error_with_code, result.code)
                    )
                }

                is ResultWrapper.Exception -> {
                    _uiState.value =
                        AuthUiState.AuthenticationFailed(result.exception.message?.let {
                            UiText.DynamicString(it)
                        } ?: UiText.StringResource(R.string.auth_error_unknown))
                }
            }
        }
    }

    private fun onNavigationHandled() {
        _uiState.update { currentState ->
            if (currentState is AuthUiState.Authenticated) {
                currentState.copy(navigateToHome = false)
            } else {
                currentState
            }
        }
    }
}