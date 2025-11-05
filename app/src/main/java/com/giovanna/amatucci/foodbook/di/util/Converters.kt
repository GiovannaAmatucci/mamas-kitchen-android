package com.giovanna.amatucci.foodbook.di.util

import androidx.room.TypeConverter
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
}