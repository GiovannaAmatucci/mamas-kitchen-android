package com.giovanna.amatucci.foodbook.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object AuthGraph

@Serializable
object AuthScreen

@Serializable
object MainGraph

@Serializable
data class DetailsScreen(val recipeId: String)


