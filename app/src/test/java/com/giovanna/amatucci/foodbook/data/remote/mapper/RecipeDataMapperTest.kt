package com.giovanna.amatucci.foodbook.data.remote.mapper

import com.giovanna.amatucci.foodbook.data.local.model.FavoriteEntity
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Direction
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Directions
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredient
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Ingredients
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.Recipe
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeCategories
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeCategory
import com.giovanna.amatucci.foodbook.data.remote.model.recipe.RecipeImages
import com.giovanna.amatucci.foodbook.data.remote.model.search.RecipeSearch
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RecipeDataMapperTest {

    private lateinit var mapper: RecipeDataMapper

    @Before
    fun setUp() {
        mapper = RecipeDataMapper()
    }

    @Test
    fun `searchRecipeDtoToDomain SHOULD map correctly`() {
        val dto = RecipeSearch(
            recipeId = "10",
            recipeName = "Pasta",
            recipeDescription = "Tasty pasta",
            recipeImage = "image_url",
            recipeIngredientsSearch = null,
            recipeNutritionSearch = null,
            recipeTypesSearch = null
        )

        val result = mapper.searchRecipeDtoToDomain(dto)

        assertEquals(10L, result.id)
        assertEquals("Pasta", result.name)
        assertEquals("Tasty pasta", result.description)
        assertEquals("image_url", result.imageUrl)
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
        assertThrows(NoSuchElementException::class.java) {
            mapper.favoriteDomainToDto(domain)
        }
    }

    @Test
    fun `favoriteEntityToDomain SHOULD map correctly`() {
        val entity = FavoriteEntity(
            recipeId = "123", name = "Burger", description = "Juicy", imageUrl = "img_url",
            imageUrls = listOf("img1", "img2"),
            preparationTime = "10",
            cookingTime = "20",
            servings = "2",
            ingredients = emptyList(),
            directions = emptyList(),
            categories = emptyList()
        )

        val result = mapper.favoriteEntityToDomain(entity)

        assertEquals(123L, result.id)
        assertEquals("Burger", result.name)
        assertEquals("Juicy", result.description)
        assertEquals("img_url", result.imageUrl)
    }

    @Test
    fun `recipeDetailDtoToDomain SHOULD map correctly with full DTO`() {
        val dto = Recipe(
            recipeId = "88",
            recipeName = "Pizza",
            recipeDescription = "Cheesy",
            recipeImages = RecipeImages(listOf("img1", "img2")),
            preparationTimeMin = "30",
            cookingTimeMin = "40",
            numberOfServings = "4",
            ingredients = Ingredients(
                ingredient = listOf(
                    Ingredient(
                        foodId = "1",
                        foodName = "tomato",
                        ingredientDescription = "desc1",
                        ingredientUrl = "url1",
                        measurementDescription = "salt",
                        numberOfUnits = "1",
                        servingId = "s1"
                    ), Ingredient(
                        foodId = "2",
                        foodName = "cheese",
                        ingredientDescription = "desc2",
                        ingredientUrl = "url2",
                        measurementDescription = "100g",
                        numberOfUnits = "1",
                        servingId = "s2"
                    )
                )
            ),
            directions = Directions(
                direction = listOf(
                    Direction(directionDescription = "Prep dough", directionNumber = "1"),
                    Direction(directionDescription = "Add cheese", directionNumber = "2")
                )
            ),
            recipeCategories = RecipeCategories(
                recipeCategory = listOf(
                    RecipeCategory("Italian", "Pizza"), RecipeCategory("Dinner", "Pizza")
                )
            )
        )

        val result = mapper.recipeDetailDtoToDomain(dto)

        assertEquals("88", result.id)
        assertEquals("Pizza", result.name)
        assertEquals("Cheesy", result.description)
        assertEquals(listOf("img1", "img2"), result.imageUrls)
        assertEquals("30", result.preparationTime)
        assertEquals("40", result.cookingTime)
        assertEquals("4", result.servings)

        // Ingredients
        assertEquals(2, result.ingredients.size)
        assertEquals("desc1", result.ingredients[0].description)
        assertEquals("tomato", result.ingredients[0].foodName)

        // Directions
        assertEquals(2, result.directions.size)
        assertEquals("1", result.directions[0].number)
        assertEquals("Prep dough", result.directions[0].description)

        // Categories
        assertEquals(listOf("Italian", "Dinner"), result.categories)
    }

    @Test
    fun `recipeDetailDtoToDomain SHOULD use default values when null`() {
        val dto = Recipe(
            recipeId = null,
            recipeName = null,
            recipeDescription = null,
            recipeImages = null,
            preparationTimeMin = null,
            cookingTimeMin = null,
            numberOfServings = null,
            ingredients = Ingredients(
                ingredient = emptyList()
            ),
            directions = Directions(
                direction = emptyList()
            ),
            recipeCategories = RecipeCategories(
                recipeCategory = emptyList()
            )
        )

        val result = mapper.recipeDetailDtoToDomain(dto)

        assertEquals("", result.id)
        assertEquals("", result.name)
        assertEquals("", result.description)
        assertNull(result.imageUrls)
        assertEquals("", result.preparationTime)
        assertEquals("", result.cookingTime)
        assertEquals("", result.servings)
        assertTrue(result.ingredients.isEmpty())
        assertTrue(result.directions.isEmpty())
        assertTrue(result.categories!!.isEmpty())
    }

    @Test(expected = NullPointerException::class)
    fun `recipeDetailDtoToDomain SHOULD throw NPE when ingredients is null`() {
        val dto = Recipe(
            recipeId = "1",
            recipeName = "Test",
            recipeDescription = "Test",
            recipeImages = null,
            preparationTimeMin = "",
            cookingTimeMin = "",
            numberOfServings = "",
            ingredients = null,
            directions = Directions(direction = listOf()),
            recipeCategories = RecipeCategories(recipeCategory = listOf())
        )

        mapper.recipeDetailDtoToDomain(dto)
    }

    @Test(expected = NullPointerException::class)
    fun `recipeDetailDtoToDomain SHOULD throw NPE when directions is null`() {
        val dto = Recipe(
            recipeId = "1",
            recipeName = "Test",
            recipeDescription = "Test",
            recipeImages = null,
            preparationTimeMin = "",
            cookingTimeMin = "",
            numberOfServings = "",
            ingredients = Ingredients(ingredient = listOf()),
            directions = null,
            recipeCategories = RecipeCategories(recipeCategory = listOf())
        )

        mapper.recipeDetailDtoToDomain(dto)
    }
}
