package com.giovanna.amatucci.foodbook.presentation.components.feedback

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionSubTitle
import com.giovanna.amatucci.foodbook.presentation.components.common.SectionTitle
import com.giovanna.amatucci.foodbook.presentation.components.image.CircularImage
import com.giovanna.amatucci.foodbook.ui.theme.AppTheme

@Composable
fun FeedbackComponent(
    title: String,
    description: String,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    subTitleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = AppTheme.dimens.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularImage(imageRes = imageRes)
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
        SectionTitle(
            title = title,
            modifier = modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingExtraSmall))
        SectionSubTitle(
            subTitle = description,
            color = subTitleColor,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = modifier.fillMaxWidth()
        )
        if (action != null) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))
            action()
        }
    }
}

