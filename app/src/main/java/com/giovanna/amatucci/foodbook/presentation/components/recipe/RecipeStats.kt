package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
private fun StatItem(
    icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(AppTheme.dimens.paddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
        SectionTitle(
            title = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        SectionSubTitle(
            subTitle = value, color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
fun RecipeStatsRow(recipe: RecipeDetails) {
    recipe.apply {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.paddingSmall,
                    horizontal = AppTheme.dimens.paddingMedium
                ),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            preparationTime?.let { preparationTime ->
                StatItem(
                    icon = Icons.Outlined.Timer,
                    label = stringResource(R.string.details_stats_prep_time),
                    value = preparationTime
                )
            }
            cookingTime?.let { cookingTime ->
                StatItem(
                    icon = Icons.Outlined.Whatshot,
                    label = stringResource(R.string.details_stats_cook_time),
                    value = cookingTime
                )
            }
            servings?.let { servings ->
                StatItem(
                    icon = Icons.Outlined.People,
                    label = stringResource(R.string.details_stats_servings),
                    value = servings
                )
            }
        }
    }
}