package com.giovanna.amatucci.foodbook.presentation.authentication.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.AuthViewModel
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthEvent
import com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state.AuthUiState
import com.giovanna.amatucci.foodbook.presentation.components.LoadingIndicator
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = uiState
    if (currentState is AuthUiState.Authenticated && currentState.navigateToHome) {
        LaunchedEffect(currentState) {
            onNavigateToHome()
            viewModel.onEvent(AuthEvent.NavigationCompleted)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is AuthUiState.Idle, is AuthUiState.Loading, is AuthUiState.Authenticated -> {
                LoadingIndicator()
            }

            is AuthUiState.AuthenticationFailed -> {
                AuthenticationFailedContent(
                    errorText = state.errorMessage.toString(),
                    onRetry = { viewModel.onEvent(AuthEvent.RequestToken) })
            }
        }
    }
}


@Composable
private fun AuthenticationFailedContent(
    errorText: String, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(Dimens.PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorText, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.common_button_retry))
        }
    }
}