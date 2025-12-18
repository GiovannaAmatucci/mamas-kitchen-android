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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.AuthViewModel
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthState
import com.giovanna.amatucci.foodbook.presentation.components.common.Loading
import com.giovanna.amatucci.foodbook.presentation.components.feedback.NetworkErrorComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthRoute(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.navigateToHome) {
        LaunchedEffect(Unit) {
            onNavigateToHome()
            viewModel.onEvent(AuthEvent.NavigationCompleted)
        }
    }
    AuthScreen(
        uiState = uiState, onRetry = { viewModel.onEvent(AuthEvent.RequestToken) })
}

@Composable
private fun AuthScreen(
    uiState: AuthState, onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState.status) {
            is ScreenStatus.Loading, ScreenStatus.Success -> {
                Loading()
            }

            is ScreenStatus.Error -> {
                NetworkErrorComponent(
                    onRetry = onRetry
                )
            }
        }
    }
}