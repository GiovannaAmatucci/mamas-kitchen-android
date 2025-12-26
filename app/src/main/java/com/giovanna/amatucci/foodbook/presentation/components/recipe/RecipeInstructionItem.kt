package com.giovanna.amatucci.foodbook.presentation.components.recipe

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun RecipeInstructionItem(
    instruction: DirectionInfo, modifier: Modifier = Modifier
) {
    instruction.apply {
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            modifier = modifier
                .padding(horizontal = AppTheme.dimens.paddingLarge)
                .animateContentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isExpanded = !isExpanded }
        ) {
            SectionTitle(
                title = "${number}.",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(AppTheme.dimens.paddingSmall))
            SectionSubTitle(
                subTitle = description,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier,
                maxLines = if (isExpanded) Int.MAX_VALUE else AppTheme.dimens.maxLinesLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}