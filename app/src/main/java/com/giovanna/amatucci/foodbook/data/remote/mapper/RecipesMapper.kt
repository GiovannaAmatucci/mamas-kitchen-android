package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Recipe
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipeSearch
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem

class RecipesMapper {
    fun searchRecipeDtoToDomain(searchDto: RecipeSearch): RecipeItem = RecipeItem(
        id = searchDto.recipeId.toLong(),
        name = searchDto.recipeName,
        description = searchDto.recipeDescription,
        imageUrl = searchDto.recipeImage
    )

    fun recipeDetailDtoToDomain(recipeDto: Recipe?): RecipeDetails =
        RecipeDetails(
            id = recipeDto?.recipeId,
            name = recipeDto?.recipeName,
            description = recipeDto?.recipeDescription,
            imageUrls = recipeDto?.recipeImages?.recipeImage ?: emptyList(),
            preparationTime = recipeDto?.preparationTimeMin?.takeIf { it.isNotBlank() },
            cookingTime = recipeDto?.cookingTimeMin,
            servings = recipeDto?.numberOfServings,
            ingredients = recipeDto?.ingredients?.ingredient?.map { ingredient ->
                ingredientDtoToDomain(
                    ingredient
                )
            }
                ?: emptyList(),
            directions = recipeDto?.directions?.direction?.map { direction ->
                directionDtoToDomain(
                    direction
                )
            }
                ?: emptyList(),
            categories = recipeDto?.recipeCategories?.recipeCategory?.map { categories -> categories.recipeCategoryName }
                ?: emptyList(),
            rating = recipeDto?.rating?.toIntOrNull())
    private fun ingredientDtoToDomain(ingredientDto: Ingredient): IngredientInfo = IngredientInfo(
        description = ingredientDto.ingredientDescription, foodName = ingredientDto.foodName
    )
    private fun directionDtoToDomain(directionDto: Direction): DirectionInfo = DirectionInfo(
        number = directionDto.directionNumber, description = directionDto.directionDescription
    )
}
