package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.PagingSource
import com.giovanna.amatucci.foodbook.data.local.db.dao.FavoritesDao
import com.giovanna.amatucci.foodbook.data.local.model.FavoritesEntity
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mapper: RecipeDataMapper

    @MockK
    private lateinit var favoritesDao: FavoritesDao

    @MockK(relaxed = true)
    private lateinit var mockPagingSource: PagingSource<Int, FavoritesEntity>

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var repository: FavoritesRepository

    private val mockRecipeDetails = RecipeDetails(
        id = "123",
        name = "Chicken Test",
        description = "A test recipe",
        imageUrls = listOf("http://example.com/img.jpg"),
        preparationTime = "10 min",
        cookingTime = "20 min",
        servings = "4",
        ingredients = emptyList(),
        directions = emptyList(),
        categories = emptyList()
    )

    private val mockFavoritesEntity = FavoritesEntity(
        recipeId = "123",
        name = "Chicken Test",
        description = "A test recipe",
        imageUrl = "http://example.com/img.jpg",
        dateFavorites = 0L,
        preparationTime = "10 min",
        cookingTime = "20 min",
        servings = "4",
        ingredients = emptyList(),
        directions = emptyList(),
        categories = emptyList(),
        imageUrls = listOf("http://example.com/img.jpg"),
        rating = 0
    )

    private val mockRecipeItem = RecipeItem(
        id = 123L,
        name = "Chicken Test",
        description = "A test recipe",
        imageUrl = "http://example.com/img.jpg",
        rating = 0
    )

    @Before
    fun setUp() {
        repository = FavoritesRepositoryImpl(mapper, favoritesDao, logWriter)
    }

    @Test
    fun `addFavorite - GIVEN recipe details - THEN calls mapper and dao insert`() = runTest {
        // ARRANGE
        every { mapper.favoriteDomainToDto(mockRecipeDetails) } returns mockFavoritesEntity
        coEvery { favoritesDao.insertFavorite(mockFavoritesEntity) } returns Unit

        // ACT
        repository.addFavorite(mockRecipeDetails)

        // ASSERT
        verify(exactly = 1) { mapper.favoriteDomainToDto(mockRecipeDetails) }
        coVerify(exactly = 1) { favoritesDao.insertFavorite(mockFavoritesEntity) }
        verify(exactly = 1) { logWriter.d(any(), any()) }
    }

    @Test
    fun `isFavorite - GIVEN recipeId - THEN calls dao and returns flow`() = runTest {
        val recipeIdString = "123"
        every { favoritesDao.isFavorite(123L) } returns flowOf(true)

        val result = repository.isFavorite(recipeIdString).first()

        assertTrue(result)
        verify(exactly = 1) { favoritesDao.isFavorite(123L) }
    }

    @Test
    fun `removeFavorite - GIVEN recipeId - THEN calls dao delete`() = runTest {
        // ARRANGE
        val recipeIdString = "123"
        coEvery { favoritesDao.deleteFavorite(recipeIdString) } returns Unit

        // ACT
        repository.removeFavorite(recipeIdString)

        // ASSERT
        coVerify(exactly = 1) { favoritesDao.deleteFavorite(recipeIdString) }
    }

    @Test
    fun `deleteAllFavorites - WHEN called - THEN calls dao deleteAllFavorites`() = runTest {
        // ARRANGE
        coEvery { favoritesDao.deleteAllFavorites() } returns Unit

        // ACT
        repository.deleteAllFavorites()

        // ASSERT
        coVerify(exactly = 1) { favoritesDao.deleteAllFavorites() }
    }

    @Test
    fun `getFavoriteDetails - GIVEN existing id - THEN returns mapped details`() = runTest {
        // ARRANGE
        val id = "123"
        coEvery { favoritesDao.getFavoriteById(id) } returns mockFavoritesEntity
        every { mapper.favoriteEntityToDetailsDomain(mockFavoritesEntity) } returns mockRecipeDetails

        // ACT
        val result = repository.getFavoriteDetails(id)

        // ASSERT
        assertEquals(mockRecipeDetails, result)
        coVerify(exactly = 1) { favoritesDao.getFavoriteById(id) }
        verify(exactly = 1) { mapper.favoriteEntityToDetailsDomain(mockFavoritesEntity) }
    }

    @Test
    fun `getFavoriteDetails - GIVEN non-existing id - THEN returns null and skips mapper`() =
        runTest {
            // ARRANGE
            val id = "999"
            coEvery { favoritesDao.getFavoriteById(id) } returns null

            // ACT
            val result = repository.getFavoriteDetails(id)

            // ASSERT
            assertEquals(null, result)
            coVerify(exactly = 1) { favoritesDao.getFavoriteById(id) }
            verify(exactly = 0) { mapper.favoriteEntityToDetailsDomain(any()) }
        }

    @Test
    fun `getLastFavorites - WHEN called - THEN maps entities to domain list`() = runTest {
        // ARRANGE
        val entitiesList = listOf(mockFavoritesEntity)
        every { favoritesDao.getLast3Favorites() } returns flowOf(entitiesList)
        every { mapper.favoriteEntityToDomain(mockFavoritesEntity) } returns mockRecipeItem

        // ACT
        val result = repository.getLastFavorites().first()

        // ASSERT
        assertEquals(1, result.size)
        assertEquals(mockRecipeItem, result.first())
        verify(exactly = 1) { favoritesDao.getLast3Favorites() }
        verify(exactly = 1) { mapper.favoriteEntityToDomain(mockFavoritesEntity) }
    }

    @Test
    fun `getFavorites - GIVEN query - THEN calls dao with formatted query`() = runTest {
        val query = "chicken"
        val formattedQuery = "%chicken%"
        every { favoritesDao.getAllFavoritesPaged(formattedQuery) } returns mockPagingSource

        val flow = repository.getFavorites(query)
        flow.firstOrNull()

        verify(exactly = 1) { favoritesDao.getAllFavoritesPaged(formattedQuery) }
    }
}
