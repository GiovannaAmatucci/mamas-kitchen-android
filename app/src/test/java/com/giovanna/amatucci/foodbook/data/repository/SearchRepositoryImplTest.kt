package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.util.LogWriter
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
    fun `getSearchQueries SHOULD return list from dao`() = runTest {
        // Arrange
        val expectedList = listOf("pizza", "sushi")
        val limit = 5
        coEvery { dao.getRecentQueries(limit) } returns expectedList

        // Act
        val result = repository.getSearchQueries(limit)

        // Assert
        assertEquals(expectedList, result)
        coVerify(exactly = 1) { dao.getRecentQueries(limit) }
    }

    @Test
    fun `getSearchQueries SHOULD return empty list and log warning WHEN dao throws exception`() =
        runTest {
            // Arrange
            val exception = RuntimeException("DB Error")
            coEvery { dao.getRecentQueries(any()) } throws exception

            // Act
            val result = repository.getSearchQueries(5)

            // Assert
            assertTrue(result.isEmpty())
            coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
        }

    @Test
    fun `saveSearchQuery SHOULD create entity and insert into dao WHEN query is valid`() = runTest {
        // Arrange
        val query = "burger"
        val slot = slot<SearchEntity>()
        coEvery { dao.insertSearch(capture(slot)) } just Runs

        // Act
        repository.saveSearchQuery(query)

        // Assert
        coVerify(exactly = 1) { dao.insertSearch(any()) }
        assertEquals(query, slot.captured.query)
    }

    @Test
    fun `saveSearchQuery SHOULD NOT call dao WHEN query is blank`() = runTest {
        // Act
        repository.saveSearchQuery("   ")

        // Assert
        coVerify(exactly = 0) { dao.insertSearch(any()) }
    }

    @Test
    fun `saveSearchQuery SHOULD log warning WHEN dao throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Insert Error")
        coEvery { dao.insertSearch(any()) } throws exception

        // Act
        repository.saveSearchQuery("pizza")

        // Assert
        coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
    }

    @Test
    fun `clearSearchHistory SHOULD call dao clear`() = runTest {
        // Arrange
        coEvery { dao.clearHistory() } just Runs

        // Act
        repository.clearSearchHistory()

        // Assert
        coVerify(exactly = 1) { dao.clearHistory() }
    }

    @Test
    fun `clearSearchHistory SHOULD log warning WHEN dao throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Delete Error")
        coEvery { dao.clearHistory() } throws exception

        // Act
        repository.clearSearchHistory()

        // Assert
        coVerify(exactly = 1) { logWriter.w(any(), any(), eq(exception)) }
    }
}