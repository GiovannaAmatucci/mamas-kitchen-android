package com.giovanna.amatucci.foodbook.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Shapes definition.
 *
 * @param extraSmall Used for very small elements, like icons with backgrounds or small tags. Radius: 4.dp.
 * @param small Used for small components like standard Chips, text fields, or small buttons. Radius: 8.dp.
 * @param medium Used for most Cards, Dialogs, BottomSheets, and medium buttons. Radius: 12.dp.
 * @param large Used for larger surfaces requiring pronounced rounding, like large FABs. Radius: 16.dp.
 * @param extraLarge Used for very large surfaces, such as some BottomSheets or full-screen elements. Radius: 28.dp.
 */
val Shape = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)