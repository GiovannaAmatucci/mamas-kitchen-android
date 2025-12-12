package com.giovanna.amatucci.foodbook.presentation.components.common

import UiConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun ChipText(
    isVisible: Boolean, textRes: Int, textColor: Color
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
    ) {
        Text(
            text = stringResource(textRes),
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = AppTheme.dimens.paddingSmall),
            maxLines = UiConstants.Layout.MAX_LINES_DEFAULT,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
    }
}