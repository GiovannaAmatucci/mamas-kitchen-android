package com.giovanna.amatucci.foodbook.data.remote.model.search


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientsSearch(@SerialName("ingredient") val ingredient: List<String>)