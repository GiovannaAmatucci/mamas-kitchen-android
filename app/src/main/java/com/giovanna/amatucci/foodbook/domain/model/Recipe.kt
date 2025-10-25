package com.giovanna.amatucci.foodbook.domain.model

data class RecipeSummary(
    val id: Int,
    val title: String,
    val imageUrl: String
)

data class RecipeDetails(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val summary: String,
    val servings: Int,
    val readyInMinutes: Int,
    val ingredients: List<String>,
    val instructions: List<String>
)