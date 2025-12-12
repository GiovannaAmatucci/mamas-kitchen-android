package com.giovanna.amatucci.foodbook.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Category(
    @StringRes val title: Int, @StringRes val query: Int, @DrawableRes val imageRes: Int
)