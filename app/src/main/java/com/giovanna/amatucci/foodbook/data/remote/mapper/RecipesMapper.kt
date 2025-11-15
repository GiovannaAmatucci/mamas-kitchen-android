package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.local.model.FavoriteEntity
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

    fun favoriteDomainToDto(recipeDomain: RecipeDetails): FavoriteEntity = FavoriteEntity(
        recipeId = recipeDomain.id.toString(),
        name = recipeDomain.name,
        description = recipeDomain.description,
        imageUrl = recipeDomain.imageUrls?.first()
    )

    fun favoriteEntityToDomain(entity: FavoriteEntity): RecipeItem = RecipeItem(
        id = entity.recipeId?.toLong(),
        name = entity.name,
        description = entity.description,
        imageUrl = entity.imageUrl
    )


    fun recipeDetailDtoToDomain(recipeDto: Recipe?): RecipeDetails = RecipeDetails(
        id = recipeDto?.recipeId ?: "",
        name = recipeDto?.recipeName ?: "",
        description = recipeDto?.recipeDescription ?: "",
        imageUrls = recipeDto?.recipeImages?.recipeImage,
        preparationTime = recipeDto?.preparationTimeMin ?: "",
        cookingTime = recipeDto?.cookingTimeMin ?: "",
        servings = recipeDto?.numberOfServings ?: "",
        ingredients = recipeDto?.ingredients?.ingredient!!.map { ingredientDtoToDomain(it) },
        directions = recipeDto.directions?.direction!!.map { directionDtoToDomain(it) },
        categories = recipeDto.recipeCategories?.recipeCategory?.map { it.recipeCategoryName })


    private fun ingredientDtoToDomain(ingredientDto: Ingredient): IngredientInfo = IngredientInfo(
        description = ingredientDto.ingredientDescription, foodName = ingredientDto.foodName
    )


    private fun directionDtoToDomain(directionDto: Direction): DirectionInfo = DirectionInfo(
        number = directionDto.directionNumber, description = directionDto.directionDescription
    )
}
