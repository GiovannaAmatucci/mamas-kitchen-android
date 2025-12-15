package com.giovanna.amatucci.foodbook.util

import androidx.room.TypeConverter
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import kotlinx.serialization.json.Json
class Converters {
    private val json = Json { ignoreUnknownKeys = true }
    @TypeConverter
    fun fromStringToList(value: String?): List<String>? {
        return value?.let { json.decodeFromString(it) }
    }
    @TypeConverter
    fun fromListToString(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>?): String =
        json.encodeToString(value ?: emptyList())

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> = json.decodeFromString(value)

    @TypeConverter
    fun fromDirectionList(value: List<Direction>?): String =
        json.encodeToString(value ?: emptyList())

    @TypeConverter
    fun toDirectionList(value: String): List<Direction> = json.decodeFromString(value)
}