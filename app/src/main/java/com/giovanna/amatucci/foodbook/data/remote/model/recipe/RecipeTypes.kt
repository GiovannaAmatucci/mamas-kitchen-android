package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeTypes(@SerialName("recipe_type") val recipeType: List<String>)