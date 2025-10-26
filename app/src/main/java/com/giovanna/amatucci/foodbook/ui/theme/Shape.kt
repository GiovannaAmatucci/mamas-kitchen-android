package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 *
 * @param extraSmall Usado para elementos muito pequenos, como ícones com backgrounds, chips com menor destaque ou tags bem pequenas.
 *                   Geralmente um raio de 4.dp.
 * @param small Usado para componentes pequenos como Chips padrão, campos de texto (OutlinedTextField, TextField), botões pequenos ou avatares.
 *              Geralmente um raio de 8.dp.
 * @param medium Usado para a maioria dos Cards, Dialogs, BottomSheets, botões maiores e outros elementos de superfície de tamanho médio.
 *               Geralmente um raio de 12.dp.
 * @param large Usado para superfícies maiores que precisam de um arredondamento mais pronunciado, como um Large Floating Action Button ou cards em destaque.
 *              Geralmente um raio de 16.dp.
 * @param extraLarge Usado para superfícies muito grandes e proeminentes, como alguns tipos de BottomSheet ou tela cheia com cantos arredondados.
 *                   Geralmente um raio de 28.dp.
 */
val Shape = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)