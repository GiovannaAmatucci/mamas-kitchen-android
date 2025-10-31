package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    @SerialName("food_id") val foodId: String,
    @SerialName("food_name") val foodName: String,
    @SerialName("ingredient_description") val ingredientDescription: String,
    @SerialName("ingredient_url") val ingredientUrl: String,
    @SerialName("measurement_description") val measurementDescription: String,
    @SerialName("number_of_units") val numberOfUnits: String,
    @SerialName("serving_id") val servingId: String
)