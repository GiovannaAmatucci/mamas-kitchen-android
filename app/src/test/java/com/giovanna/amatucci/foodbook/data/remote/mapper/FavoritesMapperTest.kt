package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.local.model.FavoritesEntity
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FavoritesMapperTest {

    private lateinit var mapper: FavoritesMapper

    @Before
    fun setUp() {
        mapper = FavoritesMapper()
    }


    @Test
    fun `favoriteDomainToDto SHOULD map correctly`() {
        val domain = RecipeDetails(
            id = "7",
            name = "Cake",
            description = "Chocolate",
            imageUrls = listOf("img1", "img2"),
            preparationTime = "10",
            cookingTime = "20",
            servings = "2",
            ingredients = emptyList(),
            directions = emptyList(),
            categories = emptyList()
        )

        val result = mapper.favoriteDomainToDto(domain)

        assertEquals("7", result.recipeId)
        assertEquals("Cake", result.name)
        assertEquals("Chocolate", result.description)
        assertEquals("img1", result.imageUrl)
    }

    @Test
    fun `favoriteDomainToDto SHOULD handle null imageUrls`() {
        val domain = RecipeDetails(
            id = "7",
            name = "Cake",
            description = "Chocolate",
            imageUrls = null,
            preparationTime = "",
            cookingTime = "",
            servings = "",
            ingredients = emptyList(),
            directions = emptyList(),
            categories = emptyList()
        )

        val result = mapper.favoriteDomainToDto(domain)

        assertNull(result.imageUrl)
    }

    @Test
    fun `favoriteDomainToDto SHOULD crash with empty imageUrls`() {
        val domain = RecipeDetails(
            id = "7",
            name = "Cake",
            description = "Chocolate",
            imageUrls = emptyList(),
            preparationTime = "",
            cookingTime = "",
            servings = "",
            ingredients = emptyList(),
            directions = emptyList(),
            categories = emptyList()
        )
        val result = mapper.favoriteDomainToDto(domain)
        assertNull(result.imageUrl)
    }

    @Test
    fun `favoriteEntityToDomain SHOULD map correctly`() {
        val entity = FavoritesEntity(
            recipeId = "123",
            name = "Burger",
            description = "Juicy",
            imageUrl = "img_url",
            imageUrls = listOf("img1", "img2"),
            preparationTime = "10",
            cookingTime = "20",
            servings = "2",
            ingredients = emptyList(),
            directions = emptyList(),
            categories = emptyList(),
            dataFavorites = 0L,
            rating = 0
        )

        val result = mapper.favoriteEntityToDomain(entity)

        assertEquals(123L, result.id)
        assertEquals("Burger", result.name)
        assertEquals("Juicy", result.description)
        assertEquals("img_url", result.imageUrl)
    }
}
