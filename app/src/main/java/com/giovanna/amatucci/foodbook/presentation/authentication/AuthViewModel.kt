package com.giovanna.amatucci.foodbook.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.UiText
import com.giovanna.amatucci.foodbook.domain.usecase.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.FetchAndSaveTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(
    private val checkAuthStatus: CheckAuthenticationStatusUseCase,
    private val fetchToken: FetchAndSaveTokenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        checkExistingToken()
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OnTokenRequest -> fetchToken()
            AuthEvent.OnNavigationHandled -> onNavigationHandled()
        }
    }

    private fun checkExistingToken() = viewModelScope.launch {
        _uiState.value = AuthUiState.Loading
        if (checkAuthStatus()) {
            _uiState.value = AuthUiState.Authenticated(navigateToHome = true)
        } else {
            fetchToken()
        }
    }


    private fun fetchToken() = viewModelScope.launch {
        _uiState.value = AuthUiState.Loading

        fetchToken.invoke().let { result ->
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