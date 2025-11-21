package com.giovanna.amatucci.foodbook.presentation.authentication.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.AuthViewModel
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthStatus
import com.giovanna.amatucci.foodbook.presentation.components.LoadingIndicatorComposable
import com.giovanna.amatucci.foodbook.presentation.components.NetworkFailedComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.navigateToHome.let { currentState ->
        if (currentState) LaunchedEffect(true) {
            onNavigateToHome()
            viewModel.onEvent(AuthEvent.NavigationCompleted)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        uiState.status.let { status ->
            when (status) {
                is AuthStatus.Loading, AuthStatus.Success -> {
                    LoadingIndicatorComposable()
                }

                is AuthStatus.Error -> {
                    NetworkFailedComposable(
                        errorMessage = stringResource(R.string.error_no_internet),
                        onRetry = { viewModel.onEvent(AuthEvent.RequestToken) })
                }
            }
        }
    }
}