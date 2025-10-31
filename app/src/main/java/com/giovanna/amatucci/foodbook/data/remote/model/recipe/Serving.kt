package com.giovanna.amatucci.foodbook.data.remote.model.recipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Serving(
    @SerialName("calcium") val calcium: String? = null,
    @SerialName("calories") val calories: String? = null,
    @SerialName("carbohydrate") val carbohydrate: String? = null,
    @SerialName("cholesterol") val cholesterol: String? = null,
    @SerialName("fat") val fat: String? = null,
    @SerialName("fiber") val fiber: String? = null,
    @SerialName("iron") val iron: String? = null,
    @SerialName("monounsaturated_fat") val monounsaturatedFat: String? = null,
    @SerialName("polyunsaturated_fat") val polyunsaturatedFat: String? = null,
    @SerialName("potassium") val potassium: String? = null,
    @SerialName("protein") val protein: String? = null,
    @SerialName("saturated_fat") val saturatedFat: String? = null,
    @SerialName("serving_size") val servingSize: String? = null,
    @SerialName("sodium") val sodium: String? = null,
    @SerialName("sugar") val sugar: String? = null,
    @SerialName("trans_fat") val transFat: String? = null,
    @SerialName("vitamin_a") val vitaminA: String? = null,
    @SerialName("vitamin_c") val vitaminC: String? = null
)