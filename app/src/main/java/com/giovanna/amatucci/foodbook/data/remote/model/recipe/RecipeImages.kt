package com.giovanna.amatucci.foodbook.data.remote.model.recipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class RecipeImages(
    @SerialName("recipe_image") val recipeImage: List<String>
)