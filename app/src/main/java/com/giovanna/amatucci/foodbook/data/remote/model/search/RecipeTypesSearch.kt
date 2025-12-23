package com.giovanna.amatucci.foodbook.data.remote.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeTypesSearch(@SerialName("recipe_type") val recipeType: List<String>)