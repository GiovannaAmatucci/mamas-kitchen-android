package com.giovanna.amatucci.foodbook.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object SearchScreen

@Serializable
data class DetailsScreen(val recipeId: Int)