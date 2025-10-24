package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    @SerialName("recipe")
    val recipe: Recipe
)