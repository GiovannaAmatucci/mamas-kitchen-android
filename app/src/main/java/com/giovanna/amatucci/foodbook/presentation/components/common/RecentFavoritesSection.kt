package com.giovanna.amatucci.foodbook.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.cards.HeroCard
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecentFavoritesSection(
    recipes: List<RecipeItem>, onRecipeClick: (String) -> Unit, modifier: Modifier
) {
    recipes.apply {
        if (isEmpty()) return
        Column(
            modifier = modifier.padding(vertical = AppTheme.dimens.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium)
        ) {
            SectionTitle(
                title = stringResource(R.string.favorites_section),
                modifier = Modifier.padding(horizontal = AppTheme.dimens.paddingMedium)
            )
            forEach { recipe ->
                HeroCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id.toString()) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
        }
    }
}