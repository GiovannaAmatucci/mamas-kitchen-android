package com.giovanna.amatucci.foodbook.presentation.components.feedback

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R

@Composable
fun NetworkErrorComponent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String = stringResource(R.string.common_error_generic)
) {
    FeedbackComponent(
        title = stringResource(R.string.error_no_internet),
        description = errorMessage,
        imageRes = R.drawable.ic_no_internet,
        modifier = modifier,
        action = {
            Button(onClick = onRetry) { Text(stringResource(R.string.common_button_retry)) }
        }
    )
}