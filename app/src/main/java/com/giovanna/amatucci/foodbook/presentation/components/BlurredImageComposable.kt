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

/**
 * Um Composable que usa uma imagem de uma URL como background desfocado para o seu conteúdo.
 *
 * @param imageUrl A URL da imagem a ser usada como background.
 * @param modifier O Modifier a ser aplicado ao Box que contém o background e o conteúdo.
 * @param blurRadius O raio do desfoque a ser aplicado à imagem de background.
 * @param content O conteúdo que será exibido sobre o background desfocado.
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