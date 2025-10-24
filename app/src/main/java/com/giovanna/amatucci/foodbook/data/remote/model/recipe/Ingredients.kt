package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredients(
    @SerialName("ingredient")
    val ingredient: List<Ingredient>
)