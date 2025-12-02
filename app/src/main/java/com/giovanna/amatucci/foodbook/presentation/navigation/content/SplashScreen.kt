package com.giovanna.amatucci.foodbook.presentation.navigation.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.util.constants.UiConstants
import kotlinx.coroutines.delay
@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(true) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) UiConstants.SPLASH_SCREEN_IF_START_ANIMATION else UiConstants.SPLASH_SCREEN_ELSE_START_ANIMATION,
        animationSpec = tween(durationMillis = UiConstants.SPLASH_SCREEN_DURATION_ANIMATION)
    )
    LaunchedEffect(key1 = true) {
        delay(UiConstants.SPLASH_SCREEN_DELAY)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alphaAnim.value)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mamas_kitchen_logo_light),
                contentDescription = stringResource(R.string.navigation_mamas_kitchen_content_description_logo)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_mamas_kitchen_text_light),
                contentDescription = stringResource(R.string.navigation_mamas_kitchen_content_description)
            )
        }
    }
}