package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeIngredientItem(
    ingredient: IngredientInfo, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.paddingLarge)
    ) {
        SectionSubTitle(
            subTitle = "• ${ingredient.description}", color = MaterialTheme.colorScheme.onSurface
        )
    }
}

