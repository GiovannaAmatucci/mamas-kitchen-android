package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.data.remote.model.search.SearchResponse
import com.giovanna.amatucci.foodbook.data.remote.network.NetworkHttpClient
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FatSecretRecipeApiImplTest {

    @MockK
    private lateinit var mockHttpClientProvider: NetworkHttpClient

    @MockK(relaxed = true)
    private lateinit var mockLogWriter: LogWriter

    private lateinit var fatSecretRecipeApi: FatSecretRecipeApiImpl

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fatSecretRecipeApi = FatSecretRecipeApiImpl(
            client = mockHttpClientProvider, logWriter = mockLogWriter
        )
    }

    private fun createMockKtorClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json)
            }
            expectSuccess = true
        }
    }

    @Test
    fun `searchRecipes WHEN the API call is successful, MUST return ResultWrapper Success`() =
        runTest {
            // ARRANGE
            val successJson =
                """{"recipes_search": {"results": { "recipe": [] },"page_number": "0","max_results": "20","total_results": "100"}}"""
            val expectedResponse = json.decodeFromString<SearchResponse>(successJson)

            val mockEngine = MockEngine {
                respond(
                    content = ByteReadChannel(successJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val mockKtorClient = createMockKtorClient(mockEngine)

            coEvery { mockHttpClientProvider() } returns mockKtorClient

            // ACT
            val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

            // ASSERT
            assertTrue("O resultado deveria ser de sucesso", result is ResultWrapper.Success)
            val successResult = result as ResultWrapper.Success
            assertEquals(expectedResponse, successResult.data)
        }

    @Test
    fun `searchRecipes WHEN API returns 400 SHOULD return a ResultWrapper Error`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine {
            respondError(HttpStatusCode.BadRequest, """{"error": "bad request"}""")
        }
        val mockKtorClient = createMockKtorClient(mockEngine)
        coEvery { mockHttpClientProvider() } returns mockKtorClient

        // ACT
        val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

        // ASSERT
        assertTrue("O resultado deveria ser de erro", result is ResultWrapper.Error)
        val errorResult = result as ResultWrapper.Error
        assertEquals("Erro do Cliente (4xx): 400 Bad Request", errorResult.message)
        assertEquals(400, errorResult.code)
    }

    @Test
    fun `searchRecipes WHEN JSON is invalid MUST return ResultWrapper Serialization Error`() =
        runTest {
            // ARRANGE
            val malformedJson = """{"recipes_search": {"results": { "recipe": [}}"""
            val mockEngine = MockEngine {
                respond(
                    content = ByteReadChannel(malformedJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val mockKtorClient = createMockKtorClient(mockEngine)
            coEvery { mockHttpClientProvider() } returns mockKtorClient

            // ACT
            val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

            // ASSERT
            assertTrue("O resultado deveria ser de erro", result is ResultWrapper.Error)
            val errorResult = result as ResultWrapper.Error
            assertEquals(-2, errorResult.code)
        }

    @Test
    fun `searchRecipes when a generic exception occurs MUST return ResultWrapper Error`() =
        runTest {
            // ARRANGE
            coEvery { mockHttpClientProvider() } throws IOException("Network Error")

            // ACT
            val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

            // ASSERT
            assertTrue("O resultado deveria ser de erro", result is ResultWrapper.Error)
        }

    @Test
    fun `getRecipeDetails when the API call is successful MUST return ResultWrapper Success`() =
        runTest {
            // ARRANGE
            val successJson =
                """{"recipes_search": {"results": { "recipe": [] },"page_number": "0","max_results": "20","total_results": "100"}}"""
            val expectedResponse = json.decodeFromString<SearchResponse>(successJson)

            val mockEngine = MockEngine {
                respond(
                    content = ByteReadChannel(successJson),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            val mockKtorClient = createMockKtorClient(mockEngine)

            coEvery { mockHttpClientProvider() } returns mockKtorClient

            // ACT
            val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

            // ASSERT
            assertTrue("O resultado deveria ser de sucesso", result is ResultWrapper.Success)
            val successResult = result as ResultWrapper.Success
            assertEquals(expectedResponse, successResult.data)
        }

    @Test
    fun `getRecipeDetails when the API returns an error should return a ResultWrapper Error`() =
        runTest {
            // ARRANGE
            val mockEngine = MockEngine {
                respondError(HttpStatusCode.BadRequest, """{"error": "bad request"}""")
            }
            val mockKtorClient = createMockKtorClient(mockEngine)
            coEvery { mockHttpClientProvider() } returns mockKtorClient

            // ACT
            val result = fatSecretRecipeApi.searchRecipes("chicken", 0, 20)

            // ASSERT
            assertTrue("O resultado deveria ser de erro", result is ResultWrapper.Error)
            val errorResult = result as ResultWrapper.Error
            assertEquals("Erro do Cliente (4xx): 400 Bad Request", errorResult.message)
            assertEquals(400, errorResult.code)
        }
}