package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

/**
 * A card component representing a single recipe.
 * Displays the recipe image and title.
 *
 * @param recipe The recipe data item to display.
 * @param onClick Callback triggered when the card is clicked.
 * @param modifier Modifier to be applied to the card.
 */
@Composable
fun RecipeCard(
    recipe: RecipeItem, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.RecipeCardHeight)
            .padding(Dimens.PaddingSmall),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.imageUrl ?: R.drawable.food_no_image, contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .height(Dimens.ImageSizeSmall)
                    .clip(RoundedCornerShape(topStart = Dimens.PaddingSmall, Dimens.PaddingSmall))
            )
            Column(
                modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = recipe.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(Dimens.PaddingSmall),
                )
            }
        }
    }
}