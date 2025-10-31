package com.giovanna.amatucci.foodbook.data.remote.model.recipe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    @SerialName("cooking_time_min") val cookingTimeMin: String? = null,
    @SerialName("directions") val directions: Directions? = null,
    @SerialName("grams_per_portion") val gramsPerPortion: String? = null,
    @SerialName("ingredients") val ingredients: Ingredients? = null,
    @SerialName("number_of_servings") val numberOfServings: String?,
    @SerialName("preparation_time_min") val preparationTimeMin: String? = null,
    @SerialName("rating") val rating: String? = null,
    @SerialName("recipe_categories") val recipeCategories: RecipeCategories,
    @SerialName("recipe_description") val recipeDescription: String? = null,
    @SerialName("recipe_id") val recipeId: String? = null,
    @SerialName("recipe_images") val recipeImages: RecipeImages? = null,
    @SerialName("recipe_name") val recipeName: String? = null,
    @SerialName("recipe_types") val recipeTypes: RecipeTypes,
    @SerialName("recipe_url") val recipeUrl: String? = null,
    @SerialName("serving_sizes") val servingSizes: ServingSizes
)
