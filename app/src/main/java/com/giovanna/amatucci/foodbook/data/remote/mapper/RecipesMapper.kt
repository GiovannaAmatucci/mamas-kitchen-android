package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Recipe
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipeSearch
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem


class RecipeDataMapper {

    fun searchRecipeDtoToDomain(searchDto: RecipeSearch): RecipeItem {
        return RecipeItem(
            id = searchDto.recipeId,
            name = searchDto.recipeName,
            description = searchDto.recipeDescription,
            imageUrl = searchDto.recipeImage
        )
    }

    fun recipeDetailDtoToDomain(recipeDto: Recipe): RecipeDetails {
        return RecipeDetails(
            id = recipeDto.recipeId,
            name = recipeDto.recipeName,
            description = recipeDto.recipeDescription,
            imageUrls = recipeDto.recipeImages?.recipeImage,
            preparationTime = recipeDto.preparationTimeMin,
            cookingTime = recipeDto.cookingTimeMin,
            servings = recipeDto.numberOfServings,
            ingredients = recipeDto.ingredients?.ingredient!!.map { ingredientDtoToDomain(it) },
            directions = recipeDto.directions?.direction!!.map { directionDtoToDomain(it) },
            categories = recipeDto.recipeCategories.recipeCategory.map { it.recipeCategoryName })
    }

    private fun ingredientDtoToDomain(ingredientDto: Ingredient): IngredientInfo {
        return IngredientInfo(
            description = ingredientDto.ingredientDescription, foodName = ingredientDto.foodName
        )
    }

    private fun directionDtoToDomain(directionDto: Direction): DirectionInfo {
        return DirectionInfo(
            number = directionDto.directionNumber, description = directionDto.directionDescription
        )
    }
}
