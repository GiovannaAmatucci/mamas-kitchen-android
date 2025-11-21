package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.usecase.auth.CheckAuthenticationStatusUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.auth.FetchAndSaveTokenUseCase
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthState
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthStatus
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

    private fun checkExistingToken() = viewModelScope.launch {
        _uiState.update { it.copy(status = AuthStatus.Loading) }
        if (checkAuthStatusUseCase.invoke()) {
            _uiState.update {
                it.copy(status = AuthStatus.Success, navigateToHome = true)
            }
        } else {
            executeTokenFetch()
        }
    }

    private fun fetchToken() = viewModelScope.launch {
        if (_uiState.value.status is AuthStatus.Loading) return@launch
        _uiState.update { it.copy(status = AuthStatus.Loading) }
        executeTokenFetch()
    }

    private suspend fun executeTokenFetch() {
        fetchTokenUseCase.invoke().let { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(status = AuthStatus.Success, navigateToHome = true)
                    }
                }

                is ResultWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            status = AuthStatus.Error,
                            navigateToHome = false,
                            error = UiText.DynamicString(result.message)
                        )
                    }
                }

                is ResultWrapper.Exception -> {
                    val errorMessage = result.exception.message?.let {
                        UiText.DynamicString(it)
                    } ?: UiText.StringResource(R.string.auth_error_unknown)

                    _uiState.update {
                        it.copy(
                            status = AuthStatus.Error, navigateToHome = false, error = errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun onNavigationHandled() {
        _uiState.update { currentState ->
            currentState.copy(navigateToHome = false)
        }
    }
}