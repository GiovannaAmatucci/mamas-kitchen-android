package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.local.model.FavoritesEntity
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Recipe
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipeSearch
import com.giovanna.amatucci.foodbook.domain.model.DirectionInfo
import com.giovanna.amatucci.foodbook.domain.model.IngredientInfo
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
class RecipeDataMapper {
    fun searchRecipeDtoToDomain(searchDto: RecipeSearch): RecipeItem = RecipeItem(
        id = searchDto.recipeId.toLong(),
        name = searchDto.recipeName,
        description = searchDto.recipeDescription,
        imageUrl = searchDto.recipeImage
    )

    fun favoriteDomainToDto(recipeDomain: RecipeDetails): FavoritesEntity = FavoritesEntity(
        recipeId = recipeDomain.id.toString(),
        name = recipeDomain.name,
        description = recipeDomain.description,
        imageUrl = recipeDomain.imageUrls?.firstOrNull(),
        imageUrls = recipeDomain.imageUrls,
        preparationTime = recipeDomain.preparationTime,
        cookingTime = recipeDomain.cookingTime,
        servings = recipeDomain.servings,
        ingredients = recipeDomain.ingredients.map { Ingredient(ingredientDescription = it.description, foodName = it.foodName) },
        directions = recipeDomain.directions.map { Direction(directionDescription = it.description, directionNumber = it.number) },
        categories = recipeDomain.categories,
        rating = recipeDomain.rating
    )

    fun favoriteEntityToDomain(entity: FavoritesEntity): RecipeItem = RecipeItem(
        id = entity.recipeId?.toLong(),
        name = entity.name,
        description = entity.description, imageUrl = entity.imageUrl, rating = entity.rating
    )

    fun favoriteEntityToDetailsDomain(entity: FavoritesEntity): RecipeDetails = RecipeDetails(
        id = entity.recipeId,
        name = entity.name,
        description = entity.description,
        imageUrls = entity.imageUrls,
        preparationTime = entity.preparationTime,
        cookingTime = entity.cookingTime,
        servings = entity.servings,
        ingredients = entity.ingredients?.map { IngredientInfo(it.foodName, it.ingredientDescription) } ?: emptyList(),
        directions = entity.directions?.map { DirectionInfo(it.directionNumber, it.directionDescription) } ?: emptyList(),
        categories = entity.categories,
        rating = entity.rating
    )

    fun recipeDetailDtoToDomain(recipeDto: Recipe?): RecipeDetails =
        RecipeDetails(
            id = recipeDto?.recipeId,
            name = recipeDto?.recipeName,
            description = recipeDto?.recipeDescription,
            imageUrls = recipeDto?.recipeImages?.recipeImage ?: emptyList(),
            preparationTime = recipeDto?.preparationTimeMin,
            cookingTime = recipeDto?.cookingTimeMin,
            servings = recipeDto?.numberOfServings,
            ingredients = recipeDto?.ingredients?.ingredient?.map { ingredientDtoToDomain(it) }
                ?: emptyList(),
            directions = recipeDto?.directions?.direction?.map { directionDtoToDomain(it) }
                ?: emptyList(),
            categories = recipeDto?.recipeCategories?.recipeCategory?.map { it.recipeCategoryName }
                ?: emptyList(),
            rating = recipeDto?.rating?.toIntOrNull())
}

    private fun ingredientDtoToDomain(ingredientDto: Ingredient): IngredientInfo = IngredientInfo(
        description = ingredientDto.ingredientDescription, foodName = ingredientDto.foodName
    )

    private fun directionDtoToDomain(directionDto: Direction): DirectionInfo = DirectionInfo(
        number = directionDto.directionNumber, description = directionDto.directionDescription
    )
