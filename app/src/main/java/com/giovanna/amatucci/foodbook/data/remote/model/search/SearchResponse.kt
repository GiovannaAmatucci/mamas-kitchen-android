package com.giovanna.amatucci.foodbook.data.remote.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("recipes")
    val recipesSearch: RecipesSearch? = null
)