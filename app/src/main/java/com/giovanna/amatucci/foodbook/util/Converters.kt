package com.giovanna.amatucci.foodbook.util

import androidx.room.TypeConverter
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import kotlinx.serialization.json.Json

/**
 * Conversores de tipo para o Room.
 * Ensina o banco de dados a salvar e ler tipos complexos que ele não entende nativamente,
 * como uma Lista de Strings.
 */
class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Converte uma String (em formato JSON) de volta para uma Lista de Strings.
     * Usado ao ler dados do banco.
     *
     * @param value A string armazenada no banco.
     * @return A Lista de Strings correspondente, ou null se a string for nula.
     */
    @TypeConverter
    fun fromStringToList(value: String?): List<String>? {
        return value?.let { json.decodeFromString(it) }
    }

    /**
     * Converte uma Lista de Strings para uma única String (em formato JSON).
     * Usado ao salvar dados no banco.
     *
     * @param list A lista de strings a ser convertida.
     * @return Uma única String em formato JSON, ou null se a lista for nula.
     */
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