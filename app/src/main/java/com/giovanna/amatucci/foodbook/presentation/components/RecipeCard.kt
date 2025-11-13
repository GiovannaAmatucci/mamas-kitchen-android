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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem

@Composable
fun RecipeCard(recipe: RecipeItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(4.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.imageUrl ?: R.drawable.food_no_image,
                contentDescription = stringResource(
                    R.string.details_section_title_instructions, recipe.name ?: ""
                ),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                SectionTitle(
                    title = recipe.name ?: ""
                )
            }
        }
    }
}
