package com.giovanna.amatucci.foodbook.data.remote.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeNutritionSearch(
    @SerialName("calories") val calories: String,
    @SerialName("carbohydrate") val carbohydrate: String,
    @SerialName("fat") val fat: String,
    @SerialName("protein") val protein: String
)