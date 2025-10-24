package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Direction(
    @SerialName("direction_description")
    val directionDescription: String,
    @SerialName("direction_number")
    val directionNumber: String
)