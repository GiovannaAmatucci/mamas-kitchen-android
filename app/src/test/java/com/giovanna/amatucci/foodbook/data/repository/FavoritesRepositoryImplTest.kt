package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.PagingSource
import com.giovanna.amatucci.foodbook.data.local.db.dao.FavoritesDao
import com.giovanna.amatucci.foodbook.data.local.model.FavoritesEntity
import com.giovanna.amatucci.foodbook.data.remote.mapper.FavoritesMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mapper: FavoritesMapper

    @MockK
    private lateinit var favoritesDao: FavoritesDao

    @MockK(relaxed = true)
    private lateinit var mockPagingSource: PagingSource<Int, FavoritesEntity>

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var repository: FavoritesRepositoryImpl
    private val mockRecipeDetails = RecipeDetails(
        id = "123",
        name = "Test Recipe",
        description = "Desc",
        imageUrls = listOf("url"),
        preparationTime = "10m",
        cookingTime = "20m",
        servings = "4",
        ingredients = emptyList(),
        directions = emptyList(),
        categories = emptyList(),
        rating = 5
    )

    private val mockEntity = FavoritesEntity(
        recipeId = "123",
        name = "Test Recipe",
        description = "Desc",
        imageUrl = "url",
        imageUrls = listOf("url"),
        preparationTime = "10m",
        cookingTime = "20m",
        servings = "4",
        ingredients = emptyList(),
        directions = emptyList(),
        categories = emptyList(),
        rating = 5
    )

    private val mockRecipeItem = RecipeItem(
        id = 123L, name = "Test Recipe", description = "Desc", imageUrl = "url", rating = 5
    )

    @Before
    fun setUp() {
        repository = FavoritesRepositoryImpl(mapper, favoritesDao, logWriter)
    }

    @Test
    fun `addFavorite - SHOULD call mapper, dao AND log success`() = runTest {
        // ARRANGE
        every { mapper.favoriteDomainToDto(mockRecipeDetails) } returns mockEntity
        coEvery { favoritesDao.insertFavorite(mockEntity) } returns Unit

        // ACT
        repository.addFavorite(mockRecipeDetails)

        // ASSERT
        verify(exactly = 1) {
            logWriter.d(
                TAG.FAVORITES_REPOSITORY,
                LogMessages.REPO_FAVORITE_ADD_START.format(mockRecipeDetails)
            )
        }
        verify(exactly = 1) { mapper.favoriteDomainToDto(mockRecipeDetails) }
        coVerify(exactly = 1) { favoritesDao.insertFavorite(mockEntity) }
    }

    @Test
    fun `getFavoriteDetails - GIVEN existing ID - SHOULD return details mapped`() = runTest {
        // ARRANGE
        val id = "123"
        coEvery { favoritesDao.getFavoriteById(id) } returns mockEntity
        every { mapper.favoriteEntityToDetailsDomain(mockEntity) } returns mockRecipeDetails

        // ACT
        val result = repository.getFavoriteDetails(id)

        // ASSERT
        assertNotNull(result)
        assertEquals(mockRecipeDetails, result)
        coVerify(exactly = 1) { favoritesDao.getFavoriteById(id) }
        verify(exactly = 1) { mapper.favoriteEntityToDetailsDomain(mockEntity) }
    }

    @Test
    fun `getFavoriteDetails - GIVEN non-existing ID - SHOULD return null and SKIP mapper`() =
        runTest {
            // ARRANGE
            val id = "999"
            coEvery { favoritesDao.getFavoriteById(id) } returns null

            // ACT
            val result = repository.getFavoriteDetails(id)

            // ASSERT
            assertNull(result)
            coVerify(exactly = 1) { favoritesDao.getFavoriteById(id) }
            verify(exactly = 0) { mapper.favoriteEntityToDetailsDomain(any()) }
        }

    @Test
    fun `isFavorite - GIVEN string ID - SHOULD convert to Long and call DAO`() = runTest {
        // ARRANGE
        val idString = "123"
        val idLong = 123L
        every { favoritesDao.isFavorite(idLong) } returns flowOf(true)

        // ACT
        val result = repository.isFavorite(idString).first()

        // ASSERT
        assertTrue(result)
        verify(exactly = 1) { favoritesDao.isFavorite(idLong) }
    }

    @Test
    fun `getLastFavorites - SHOULD collect list from DAO and map items`() = runTest {
        // ARRANGE
        val entities = listOf(mockEntity)
        every { favoritesDao.getLast3Favorites() } returns flowOf(entities)
        every { mapper.favoriteEntityToDomain(mockEntity) } returns mockRecipeItem

        // ACT
        val resultList = repository.getLastFavorites().first()

        // ASSERT
        assertEquals(1, resultList.size)
        assertEquals(mockRecipeItem, resultList[0])

        verify(exactly = 1) { favoritesDao.getLast3Favorites() }
        verify(exactly = 1) { mapper.favoriteEntityToDomain(mockEntity) }
    }

    @Test
    fun `removeFavorite - SHOULD call DAO and log`() = runTest {
        // ARRANGE
        val id = "123"
        coEvery { favoritesDao.deleteFavorite(id) } returns Unit

        // ACT
        repository.removeFavorite(id)

        // ASSERT
        verify(exactly = 1) {
            logWriter.d(
                TAG.FAVORITES_REPOSITORY, LogMessages.REPO_FAVORITE_REMOVE_START.format(id)
            )
        }
        coVerify(exactly = 1) { favoritesDao.deleteFavorite(id) }
    }

    @Test
    fun `deleteAllFavorites - SHOULD call DAO`() = runTest {
        // ARRANGE
        coEvery { favoritesDao.deleteAllFavorites() } returns Unit

        // ACT
        repository.deleteAllFavorites()

        // ASSERT
        coVerify(exactly = 1) { favoritesDao.deleteAllFavorites() }
    }
}