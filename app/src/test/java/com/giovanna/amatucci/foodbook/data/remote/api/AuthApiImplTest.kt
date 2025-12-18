package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
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
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AuthApiImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var mockHttpClientProvider: TokenHttpClient

    @MockK(relaxed = true)
    private lateinit var mockLogWriter: LogWriter

    private lateinit var authApi: AuthApi

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun createMockKtorClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(json)
            }
            expectSuccess = true
        }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        authApi = AuthApiImpl(
            logWriter = mockLogWriter,
            client = mockHttpClientProvider
        )
    }

    @Test
    fun `getAccessToken - GIVEN request successfully - THEN returns ResultWrapper Success`() =
        runTest {
            // ARRANGE
            val successJson = """
            {
                "access_token": "fake-jwt-token-123",
                "expires_in": 3600,
                "token_type": "Bearer"
            }
        """.trimIndent()
            val expectedResponse = json.decodeFromString<TokenResponse>(successJson)

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
            val result = authApi.getAccessToken()

            // ASSERT
            assertTrue(
                "O resultado deveria ser ResultWrapper.Success",
                result is ResultWrapper.Success
            )
            val successResult = result as ResultWrapper.Success
            assertEquals(expectedResponse, successResult.data)

            verify(exactly = 1) {
                mockLogWriter.d(
                    eq("AuthApi"),
                    eq(LogMessages.AUTH_TOKEN_REQUEST)
                )
            }
            verify(exactly = 1) {
                mockLogWriter.d(
                    eq("AuthApi"),
                    eq(LogMessages.AUTH_TOKEN_SUCCESS)
                )
            }
            verify(exactly = 0) { mockLogWriter.e(any(), any(), any()) }
        }

    @Test
    fun `getAccessToken - GIVEN server error (401) - Then returns ResultWrapper Error`() = runTest {
        // ARRANGE
        val mockEngine = MockEngine {
            respondError(HttpStatusCode.Unauthorized, """{"error": "unauthorized"}""")
        }
        val mockKtorClient = createMockKtorClient(mockEngine)
        coEvery { mockHttpClientProvider() } returns mockKtorClient

        // ACT
        val result = authApi.getAccessToken()

        // ASSERT
        assertTrue("O resultado deveria ser ResultWrapper.Error", result is ResultWrapper.Error)
        val errorResult = result as ResultWrapper.Error
        assertEquals(401, errorResult.code)

        verify(exactly = 1) { mockLogWriter.d(eq("AuthApi"), eq(LogMessages.AUTH_TOKEN_REQUEST)) }
        verify(exactly = 1) { mockLogWriter.e(eq("AuthApi"), any(), any()) }
    }

    @Test
    fun `getAccessToken - GIVEN generic exception (IOException) - THEN returns ResultWrapper Exception`() =
        runTest {
            val networkException = IOException("No internet")
            coEvery { mockHttpClientProvider() } throws networkException

            val result = authApi.getAccessToken()
            assertTrue("Deveria ser Error", result is ResultWrapper.Error)

            val errorResult = result as ResultWrapper.Error
            assertEquals(-1, errorResult.code) // BaseApi define -1 para IOException
        }
}