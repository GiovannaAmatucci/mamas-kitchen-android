package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    @SerialName("food_id") val foodId: String? = null,
    @SerialName("food_name") val foodName: String? = null,
    @SerialName("ingredient_description") val ingredientDescription: String? = null,
    @SerialName("ingredient_url") val ingredientUrl: String? = null,
    @SerialName("measurement_description") val measurementDescription: String? = null,
    @SerialName("number_of_units") val numberOfUnits: String? = null,
    @SerialName("serving_id") val servingId: String? = null,
)
