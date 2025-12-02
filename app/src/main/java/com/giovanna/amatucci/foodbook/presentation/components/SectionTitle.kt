package com.giovanna.amatucci.foodbook.presentation.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

/**
 * A standard section title text component used throughout the app.
 *
 * @param title The text content of the title.
 * @param modifier Modifier to be applied to the text.
 */
@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = modifier.padding(Dimens.PaddingSmall),
        color = MaterialTheme.colorScheme.onSurface
    )
}