package com.giovanna.amatucci.foodbook.data.repository


import com.giovanna.amatucci.foodbook.data.local.db.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    @MockK
    lateinit var dao: SearchDao

    @MockK(relaxed = true)
    lateinit var logWriter: LogWriter
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SearchRepositoryImpl(dao, logWriter)
    }

    @Test
    fun `getSearchQueries SHOULD return query list WHEN history exists`() = runTest {
        // Arrange
        val expectedQueries = listOf("pizza", "sushi")
        val searchEntity = SearchEntity(id = 1, queries = expectedQueries)
        coEvery { dao.getSearchHistory() } returns searchEntity

        // Act
        val result = repository.getSearchQueries()

        // Assert
        assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { dao.getSearchHistory() }
    }

    @Test
    fun `getSearchQueries SHOULD return empty list WHEN history is null`() = runTest {
        // Arrange
        coEvery { dao.getSearchHistory() } returns null

        // Act
        val result = repository.getSearchQueries()

        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getSearchQueries SHOULD return empty list WHEN history queries are null or empty`() =
        runTest {
            // Arrange
            val searchEntity = SearchEntity(id = 1, queries = emptyList())
            coEvery { dao.getSearchHistory() } returns searchEntity

            // Act
            val result = repository.getSearchQueries()

            // Assert
            assertTrue(result.isEmpty())
        }

    @Test
    fun `getSearchQueries SHOULD return empty list and log warning WHEN dao throws exception`() =
        runTest {
            // Arrange
            val exception = RuntimeException("DB Error")
            coEvery { dao.getSearchHistory() } throws exception

            // Act
            val result = repository.getSearchQueries()

            // Assert
            assertTrue(result.isEmpty())
            coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
        }

    @Test
    fun `saveSearchQuery SHOULD add new query to start WHEN history is null`() = runTest {
        // Arrange
        val newQuery = "burger"
        coEvery { dao.getSearchHistory() } returns null

        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.saveSearchQuery(newQuery)

        // Assert
        coVerify(exactly = 1) { dao.insertSearch(any()) }

        val capturedEntity = slot.captured
        assertEquals(0, capturedEntity.id)
        assertEquals(listOf(newQuery), capturedEntity.queries)
    }

    @Test
    fun `saveSearchQuery should ADD new query to start WHEN history exists`() = runTest {
        // Arrange
        val newQuery = "pizza"
        val existingQueries = listOf("sushi", "pasta")
        val existingEntity = SearchEntity(id = 1, queries = existingQueries)

        coEvery { dao.getSearchHistory() } returns existingEntity

        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.saveSearchQuery(newQuery)

        // Assert
        val capturedEntity = slot.captured
        assertEquals(1, capturedEntity.id)
        assertEquals(listOf(newQuery, "sushi", "pasta"), capturedEntity.queries)
    }

    @Test
    fun `saveSearchQuery SHOULD move existing query to start`() = runTest {
        // Arrange
        val queryToMove = "pizza"
        val existingQueries = listOf("sushi", "pizza", "pasta")
        val existingEntity = SearchEntity(id = 1, queries = existingQueries)

        coEvery { dao.getSearchHistory() } returns existingEntity

        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.saveSearchQuery(queryToMove)

        // Assert
        val capturedEntity = slot.captured
        assertEquals(1, capturedEntity.id)
        assertEquals(listOf("pizza", "sushi", "pasta"), capturedEntity.queries)
    }

    @Test
    fun `saveSearchQuery SHOULD do nothing WHEN query is blank`() = runTest {
        // Act
        repository.saveSearchQuery("   ")

        // Assert
        coVerify(exactly = 0) { dao.getSearchHistory() }
        coVerify(exactly = 0) { dao.insertSearch(any()) }
    }

    @Test
    fun `saveSearchQuery SHOULD log warning WHEN dao throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("DB Error")
        coEvery { dao.getSearchHistory() } throws exception

        // Act
        repository.saveSearchQuery("pizza")

        // Assert
        coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
        coVerify(exactly = 0) { dao.insertSearch(any()) }
    }

    @Test
    fun `clearSearchHistory SHOULD save empty list WHEN history exists`() = runTest {
        // Arrange
        val existingEntity = SearchEntity(id = 5, queries = listOf("a", "b", "c"))
        coEvery { dao.getSearchHistory() } returns existingEntity

        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.clearSearchHistory()

        // Assert
        val capturedEntity = slot.captured
        assertEquals(5, capturedEntity.id)
        assertTrue(capturedEntity.queries?.isEmpty() ?: true)
    }

    @Test
    fun `clearSearchHistory SHOULD save empty list WHEN history is null`() = runTest {
        // Arrange
        coEvery { dao.getSearchHistory() } returns null

        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.clearSearchHistory()

        // Assert
        val capturedEntity = slot.captured
        assertEquals(0, capturedEntity.id)
        assertTrue(capturedEntity.queries?.isEmpty() ?: true)
    }

    @Test
    fun `clearSearchHistory SHOULD log warning WHEN dao throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("DB Error")
        coEvery { dao.getSearchHistory() } throws exception

        // Act
        repository.clearSearchHistory()

        // Assert
        coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
        coVerify(exactly = 0) { dao.insertSearch(any()) }
    }
}