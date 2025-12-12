package com.giovanna.amatucci.foodbook.presentation.components.common


import UiConstants
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    textAlign: TextAlign = TextAlign.Start,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = UiConstants.Layout.MAX_LINES_DEFAULT
) {
    Text(
        text = title,
        style = style,
        overflow = TextOverflow.Ellipsis,
        fontWeight = fontWeight,
        maxLines = maxLines,
        textAlign = textAlign,
        modifier = modifier,
        color = color
    )
}