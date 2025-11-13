package com.giovanna.amatucci.foodbook.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.LoadingIndicator
import com.giovanna.amatucci.foodbook.presentation.components.RecipeImages
import com.giovanna.amatucci.foodbook.presentation.components.SectionTitle
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

@Composable
fun DetailsContent(
    modifier: Modifier, status: DetailsStatus, recipe: RecipeDetails?
) {
    when (status) {
        DetailsStatus.Loading -> LoadingIndicator()
        DetailsStatus.Error -> EmptyMessage(message = stringResource(R.string.details_error_message_loading_failed))
        DetailsStatus.Success -> {
            recipe?.let {
                DetailsList(recipe = it, modifier = modifier)
            }
        }
    }
}

@Composable
private fun DetailsList(recipe: RecipeDetails, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingMedium)
    ) {
        item {
            DetailsHeader(recipe = recipe)
        }

        item {
            DetailsStatsRow(recipe = recipe)
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        }

        item {
            SectionTitle(stringResource(R.string.details_section_title_ingredients))
        }
        items(recipe.ingredients) { ingredient ->
            DetailsIngredientItem(ingredient = ingredient)
        }
        item {
            SectionTitle(stringResource(R.string.details_section_title_instructions))
        }
        items(recipe.directions) { directions ->
            DetailsInstructionItem(instruction = directions)
        }
    }
}

@Composable
private fun DetailsHeader(recipe: RecipeDetails) {
    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
    RecipeImages(
        images = recipe.imageUrls ?: emptyList()
    )
    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
    Text(
        text = recipe.name ?: "", style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
    Text(
        text = recipe.description ?: "", style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
}

@Composable
private fun DetailsStatsRow(recipe: RecipeDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingSmall),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        recipe.preparationTime?.let {
            DetailsStatItem(
                icon = Icons.Outlined.Timer,
                label = stringResource(R.string.details_stats_prep_time),
                value = it
            )
        }
        recipe.cookingTime?.let {
            DetailsStatItem(
                icon = Icons.Outlined.Whatshot,
                label = stringResource(R.string.details_stats_cook_time),
                value = it
            )
        }
        recipe.servings?.let {
            DetailsStatItem(
                icon = Icons.Outlined.People,
                label = stringResource(R.string.details_stats_servings),
                value = it
            )
        }
    }
}

@Composable
private fun DetailsStatItem(
    icon: ImageVector, label: String, value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetailsIngredientItem(ingredient: IngredientInfo) {
    Text(
        text = "• ${ingredient.description}", modifier = Modifier.padding(
            start = Dimens.PaddingSmall, bottom = Dimens.PaddingExtraSmall
        ), style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun DetailsInstructionItem(instruction: DirectionInfo) {
    Row(modifier = Modifier.padding(bottom = Dimens.PaddingSmall)) {
        Text(
            text = "${instruction.number}.",
            modifier = Modifier.width(Dimens.PaddingExtraLarge),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = instruction.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}