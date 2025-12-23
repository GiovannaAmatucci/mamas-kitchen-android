package com.giovanna.amatucci.foodbook.data.remote.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearch(
    @SerialName("recipe_description")
    val recipeDescription: String? = null,
    @SerialName("recipe_id")
    val recipeId: String,
    @SerialName("recipe_image")
    val recipeImage: String? = null,
    @SerialName("recipe_ingredients")
    val recipeIngredientsSearch: RecipeIngredientsSearch?,
    @SerialName("recipe_name")
    val recipeName: String? = null,
    @SerialName("recipe_nutrition")
    val recipeNutritionSearch: RecipeNutritionSearch?,
    @SerialName("recipe_types")
    val recipeTypesSearch: RecipeTypesSearch?
)