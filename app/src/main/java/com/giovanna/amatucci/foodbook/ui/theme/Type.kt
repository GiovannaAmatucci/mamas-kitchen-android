package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * @param displayLarge Usado para textos grandes e expressivos, para chamar atenção. Ideal para títulos muito curtos, numerais ou designs de heróis.
 *                     Não é frequentemente usado, mas disponível para alto impacto.
 *
 * @param displaySmall Usado para textos grandes e importantes, como títulos de introdução ou numerais em destaque.
 *                     Geralmente o maior texto que você verá em uma tela.
 * @param displayMedium Usado para textos grandes, mas com impacto ligeiramente menor que [displayLarge].
 * @param headlineLarge Usado para títulos de tela de alto nível. Para títulos mais curtos e com maior ênfase.
 *                      Ex: Título da tela "Receitas Favoritas".
 * @param headlineMedium Usado para títulos de tela ou seções importantes, como o nome da receita em `DetailsScreen`.
 *                       Ex: "Bolo de Cenoura com Cobertura de Chocolate".
 * @param headlineSmall Usado para títulos de tela ou seções de menor ênfase que as headlines maiores.
 * @param titleLarge Usado para títulos de seções, cabeçalhos de componentes ou elementos de lista proeminentes.
 *                   Ex: Título de "Ingredientes" ou "Instruções" em `DetailsScreen`.
 * @param titleMedium Usado para títulos dentro de componentes menores, como o título de uma receita em `RecipeListItem`.
 *                    Normalmente tem um peso de fonte `Medium`.
 * @param titleSmall Usado para subtítulos, etiquetas de categorias ou informações secundárias em títulos.
 * @param bodyLarge Usado para o corpo principal do texto em um aplicativo, como descrições de receitas.
 *                  É o estilo padrão para a maioria dos textos longos.
 * @param bodyMedium Usado para textos de corpo de menor destaque ou para parágrafos mais curtos.
 *                   Ex: Texto de resumo da receita em `DetailsScreen` se não for o principal.
 * @param bodySmall Usado para textos de corpo menores, como legendas, notas de rodapé ou informações legais.
 * @param labelMedium Usado para texto auxiliar, como hints, etiquetas de ícones em navegação inferior ou tags pequenas.
 * @param labelLarge Usado para texto em botões, campos de texto (labels/placeholders), ou outros elementos interativos.
 *                   Normalmente tem um peso de fonte `Medium`.
 *                   Ex: Texto do botão "Buscar" em `SearchScreen`.
 * @param labelSmall Usado para texto de menor tamanho, como rótulos de tempo, descrições de status ou mensagens de erro sutis.
 */
val Typography = Typography(

    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, // Pode ser alterado para semibold/bold se a fonte permitir
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ), displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),

    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ), headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),


    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)