package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun SectionSubTitle(
    subTitle: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    lineHeight: TextUnit = TextUnit.Unspecified,
    maxLines: Int = AppTheme.dimens.maxLinesDefault
) {
    Text(
        text = subTitle,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = textAlign,
        modifier = modifier,
        maxLines = maxLines,
        lineHeight = lineHeight,
        overflow = textOverflow
    )
}