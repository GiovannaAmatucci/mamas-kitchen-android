package com.giovanna.amatucci.foodbook.presentation.authentication

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.componets.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = uiState
    if (currentState is AuthUiState.Authenticated && currentState.navigateToHome) {
        LaunchedEffect(Unit) {
            onNavigateToHome()
            viewModel.onEvent(AuthEvent.OnNavigationHandled)
        }
    }
    when (val state = uiState) {
        is AuthUiState.Loading -> {
            LoadingIndicator()
        }
        is AuthUiState.AuthenticationFailed -> {
           Button(onClick = { viewModel.onEvent(AuthEvent.OnTokenRequest) }) {
               Text("Tentar novamente")
           }
        }
        is AuthUiState.Idle, is AuthUiState.Authenticated -> {
           LoadingIndicator()
        }
    }
}