package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.PagingSource
import com.giovanna.amatucci.foodbook.data.local.db.FavoriteDao
import com.giovanna.amatucci.foodbook.data.local.model.FavoriteEntity
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository
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
    private lateinit var favoriteDao: FavoriteDao

    @MockK(relaxed = true)
    private lateinit var mockPagingSource: PagingSource<Int, FavoriteEntity>

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var repository: FavoriteRepository

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

    private val mockFavoriteEntity = FavoriteEntity(
        recipeId = "123",
        name = "Chicken Test",
        description = "A test recipe",
        imageUrl = "http://example.com/img.jpg",
        dateFavorites = 0L
    )

    @Before
    fun setUp() {
        repository = FavoriteRepositoryImpl(mapper, favoriteDao, logWriter)
    }

    @Test
    fun `addFavorite - GIVEN recipe details - THEN calls mapper and dao insert`() = runTest {
        // ARRANGE
        every { mapper.favoriteDomainToDto(mockRecipeDetails) } returns mockFavoriteEntity
        coEvery { favoriteDao.insertFavorite(mockFavoriteEntity) } returns Unit

        // ACT
        repository.addFavorite(mockRecipeDetails)

        // ASSERT
        verify(exactly = 1) { mapper.favoriteDomainToDto(mockRecipeDetails) }
        coVerify(exactly = 1) { favoriteDao.insertFavorite(mockFavoriteEntity) }
    }

    @Test
    fun `isFavorite - GIVEN recipeId - THEN calls dao with Long id and returns flow value`() =
        runTest {
            // ARRANGE
            val recipeIdString = "123"
            val recipeIdLong = 123L
            every { favoriteDao.isFavorite(recipeIdLong) } returns flowOf(true)

            // ACT
            val result = repository.isFavorite(recipeIdString).first()

            // ASSERT
            assertTrue(result)
            verify(exactly = 1) { favoriteDao.isFavorite(recipeIdLong) }
        }

    @Test
    fun `removeFavorite - GIVEN recipeId - THEN calls dao delete`() = runTest {
        // ARRANGE
        val recipeIdString = "123"
        coEvery { favoriteDao.deleteFavorite(recipeIdString) } returns Unit

        // ACT
        repository.removeFavorite(recipeIdString)

        // ASSERT
        coVerify(exactly = 1) { favoriteDao.deleteFavorite(recipeIdString) }
    }

    @Test
    fun `deleteAllFavorites - WHEN called - THEN calls dao deleteAllFavorites`() = runTest {
        // ARRANGE
        coEvery { favoriteDao.deleteAllFavorites() } returns Unit

        // ACT
        repository.deleteAllFavorites()

        // ASSERT
        coVerify(exactly = 1) { favoriteDao.deleteAllFavorites() }
    }

    @Test
    fun `getFavorites - GIVEN query - THEN calls dao with formatted query and maps PagingData`() =
        runTest {
            // ARRANGE
            val query = "chicken"
            val formattedQuery = "%chicken%"
            every { favoriteDao.getAllFavoritesPaged(eq(formattedQuery)) } returns mockPagingSource

            // ACT
            val flow = repository.getFavorites(query)
            flow.firstOrNull()

            // ASSERT
            verify(exactly = 1) { favoriteDao.getAllFavoritesPaged(formattedQuery) }
        }
}