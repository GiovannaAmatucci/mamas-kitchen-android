package com.giovanna.amatucci.foodbook.presentation.components.search

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.common.RecentFavoritesSection
import com.giovanna.amatucci.foodbook.presentation.components.feedback.FeedbackComponent

@Composable
fun SearchInitialSection(
    recentFavorites: List<RecipeItem>?,
    onRecipeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int = R.string.search_idle_message,
    @StringRes descriptionRes: Int = R.string.search_description_message,
    imageRes: Int = R.drawable.ic_search_recipes
) {
    val hasFavorites = recentFavorites?.isNotEmpty() == true
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = if (hasFavorites) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasFavorites) {
            RecentFavoritesSection(
                recipes = recentFavorites,
                onRecipeClick = onRecipeClick,
                modifier = Modifier.fillMaxSize()
            )
        } else if (recentFavorites != null) {
            FeedbackComponent(
                title = stringResource(titleRes),
                description = stringResource(descriptionRes),
                imageRes = imageRes
            )
        }
    }
}