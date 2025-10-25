package com.giovanna.amatucci.foodbook.data.remote.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipesSearch(
    @SerialName("max_results")
    val maxResults: String,
    @SerialName("page_number")
    val pageNumber: String,
    @SerialName("recipe")
    val recipeSearch: List<RecipeSearch>? = null,
    @SerialName("total_results")
    val totalResults: String
)