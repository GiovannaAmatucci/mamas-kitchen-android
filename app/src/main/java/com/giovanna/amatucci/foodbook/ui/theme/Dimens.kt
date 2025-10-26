package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.AppBarHeight
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.CardElevation
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.IconSizeMedium
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.ImageSizeLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingExtraLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingExtraSmall
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingLarge
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingMedium
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.PaddingSmall
import com.giovanna.amatucci.foodbook.ui.theme.Dimens.ScreenPadding


/**
 * ### Espaçamentos e Tamanhos Comuns
 *
 * @param PaddingExtraSmall Espaçamento ou tamanho muito pequeno (e.g., para elementos de layout minimalistas ou bordas finas).
 * @param PaddingSmall Espaçamento ou tamanho pequeno (e.g., entre elementos próximos, ícones e texto).
 * @param PaddingMedium Espaçamento ou tamanho médio, um valor de uso geral para a maioria dos layouts (e.g., entre seções, padding de cartões).
 * @param PaddingLarge Espaçamento ou tamanho grande, para criar separação clara entre seções principais ou padding de tela.
 * @param PaddingExtraLarge Espaçamento ou tamanho muito grande, para criar um impacto visual significativo ou margens amplas.
 *
 * @param ScreenPadding O padding padrão aplicado nas bordas da maioria das telas.
 * @param CardElevation Elevação padrão para cards e superfícies que flutuam ligeiramente acima do background.
 * @param IconSizeMedium Tamanho médio padrão para ícones.
 * @param ImageSizeLarge Tamanho grande padrão para imagens principais ou de destaque.
 * @param AppBarHeight Altura padrão para a barra superior (TopAppBar).
 */
object Dimens {
    val PaddingExtraSmall = 4.dp
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp
    val PaddingExtraLarge = 32.dp

    val ScreenPadding = 16.dp
    val CardElevation = 2.dp

    val IconSizeMedium = 24.dp
    val ImageSizeLarge = 200.dp

    val AppBarHeight = 56.dp
}