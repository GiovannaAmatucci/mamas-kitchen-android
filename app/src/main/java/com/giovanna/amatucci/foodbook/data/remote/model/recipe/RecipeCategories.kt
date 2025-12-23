package com.giovanna.amatucci.foodbook.data.remote.model.recipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCategories(
    @SerialName("recipe_category")
    val recipeCategory: List<RecipeCategory>
)