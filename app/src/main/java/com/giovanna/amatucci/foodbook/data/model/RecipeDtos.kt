package com.giovanna.amatucci.foodbook.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchResponseDto(
    val results: List<RecipeSearchResultDto>, val totalResults: Int
)

@Serializable
data class RecipeSearchResultDto(
    val id: Int, val title: String, val image: String
)

@Serializable
data class RecipeInformationDto(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val servings: Int,
    val readyInMinutes: Int,
    @SerialName("extendedIngredients") val ingredients: List<ExtendedIngredientDto>,
    @SerialName("analyzedInstructions") val instructions: List<AnalyzedInstructionDto>
)

@Serializable
data class ExtendedIngredientDto(
    val id: Int, val original: String
)

@Serializable
data class AnalyzedInstructionDto(
    val name: String, val steps: List<StepDto>
)

@Serializable
data class StepDto(
    val number: Int, val step: String
)