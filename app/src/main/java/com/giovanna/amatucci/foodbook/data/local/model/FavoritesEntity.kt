package com.giovanna.amatucci.foodbook.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient

@Entity(tableName = "favoritesEntity")
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo("image_url") val imageUrl: String?,
    @ColumnInfo("image_urls") val imageUrls: List<String>?,
    @ColumnInfo("data_favorites") val dataFavorites: Long = System.currentTimeMillis(),
    @ColumnInfo("preparation_time") val preparationTime: String?,
    @ColumnInfo("cooking_time") val cookingTime: String?,
    @ColumnInfo("servings") val servings: String?,
    @ColumnInfo("ingredients") val ingredients: List<Ingredient>?,
    @ColumnInfo("directions") val directions: List<Direction>?,
    @ColumnInfo("categories") val categories: List<String>?,
    @ColumnInfo("rating") val rating: Int?

)