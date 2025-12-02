package com.giovanna.amatucci.foodbook.presentation.details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.components.DetailsImageComposable
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.RecipeDetailsShimmer
import com.giovanna.amatucci.foodbook.presentation.components.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsStatus
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import com.giovanna.amatucci.foodbook.ui.theme.rememberScrimColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    modifier: Modifier,
    status: DetailsStatus,
    recipe: RecipeDetails?,
    onImageDisplayed: (String?) -> Unit = {}
) {
    val scrimColor = rememberScrimColor()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(scrimColor),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            is DetailsStatus.Loading -> {
                RecipeDetailsShimmer()
            }

            is DetailsStatus.Error -> EmptyMessage(message = stringResource(R.string.details_error_message_loading_failed))

            is DetailsStatus.Success -> {
                recipe?.let { recipe ->
                    DetailsList(
                        recipe = recipe,
                        modifier = Modifier.fillMaxSize(),
                        onImageDisplayed = onImageDisplayed,
                    )
                }
            }
        }
    }
}
@Composable
private fun DetailsList(
    recipe: RecipeDetails, modifier: Modifier, onImageDisplayed: (String?) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            DetailsImageComposable(
                images = recipe.imageUrls ?: emptyList(), onImageDisplayed = onImageDisplayed
            )
        }
        item {
            DetailsHeader(recipe = recipe)
        }

        item {
            DetailsRow(recipe = recipe)
            Spacer(modifier = Modifier.padding(Dimens.PaddingMedium))
        }

        item {
            SectionTitle(
                stringResource(R.string.details_section_title_ingredients),
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
            )
        }
        items(
            items = recipe.ingredients,
            key = { ingredient -> ingredient.description.toString() }) { ingredient ->
            DetailsIngredientItem(ingredient = ingredient)
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }
        item {
            SectionTitle(
                stringResource(R.string.details_section_title_instructions),
                modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }
        items(
            items = recipe.directions, key = { direction -> direction.number }) { directions ->
            DetailsInstructionItem(instruction = directions)
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }
        item {
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        }
    }
}

@Composable
private fun DetailsHeader(recipe: RecipeDetails) {
    Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
        recipe.name?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }
        recipe.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        }
    }
}
@Composable
private fun DetailsRow(recipe: RecipeDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingSmall)
            .padding(horizontal = Dimens.PaddingMedium),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
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
    icon: ImageVector, contentDescription: String? = null, label: String, value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(Dimens.PaddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun DetailsIngredientItem(ingredient: IngredientInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingMedium)
    ) {
        Text(
            text = "• ${ingredient.description}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}

@Composable
private fun DetailsInstructionItem(instruction: DirectionInfo) {
    Row(
        modifier = Modifier.padding(horizontal = Dimens.PaddingLarge),
    ) {
        Text(
            text = "${instruction.number}.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
        Text(
            text = instruction.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}