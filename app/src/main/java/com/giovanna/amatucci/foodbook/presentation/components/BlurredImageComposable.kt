package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.giovanna.amatucci.foodbook.R

/**•A Composable that uses an image from a URL as a blurred background for its content.
 * @param imageUrl The URL of the image to be used as background.
 * @param modifier The Modifier to be applied to the Box containing the background and content.
 * @param blurRadius The blur radius to be applied to the background image.
 * @param content The content to be displayed over the blurred background.
 */
@Composable
fun BlurredImageComposable(
    imageUrl: String?,
    modifier: Modifier = Modifier, blurRadius: Dp = 20.dp, content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.food_no_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius),
                contentScale = ContentScale.Crop
            )
        }
        content()
    }
}