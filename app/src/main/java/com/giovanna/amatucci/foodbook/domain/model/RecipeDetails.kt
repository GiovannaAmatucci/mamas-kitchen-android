package com.giovanna.amatucci.foodbook.domain.model


data class RecipeDetails(
    val id: String?,
    val name: String?,
    val description: String? = null,
    val imageUrls: List<String>? = null,
    val preparationTime: String?,
    val cookingTime: String?,
    val servings: String?,
    val ingredients: List<IngredientInfo>,
    val directions: List<DirectionInfo>,
    val categories: List<String>?,
    val rating: Int? = 0
)