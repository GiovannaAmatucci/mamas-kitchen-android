package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCategory(
    @SerialName("recipe_category_name")
    val recipeCategoryName: String,
    @SerialName("recipe_category_url")
    val recipeCategoryUrl: String
)