package com.giovanna.amatucci.foodbook.data.repository

// 'verify' normal não é mais necessário para suspend functions
import android.database.sqlite.SQLiteException
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var authApi: AuthApi

    @MockK
    private lateinit var tokenRepository: TokenRepository

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var authRepository: AuthRepositoryImpl

    private val mockTokenResponse = TokenResponse(
        accessToken = "fake-token-123",
        tokenType = "Bearer",
        expiresIn = 3600
    )
    private val apiSuccessResult = ResultWrapper.Success(mockTokenResponse)

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(authApi, tokenRepository, logWriter)
    }

    @Test
    fun `fetchAndSaveToken - GIVEN API and DB work - THEN save token and return Success`() =
        runTest {
            // ARRANGE
            coEvery { authApi.getAccessToken() } returns apiSuccessResult
            coEvery { tokenRepository.clearToken() } returns Unit
            coEvery { tokenRepository.saveToken(mockTokenResponse) } returns Unit

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("O resultado deveria ser Success", result is ResultWrapper.Success)
            assertEquals(mockTokenResponse, (result as ResultWrapper.Success).data)
            coVerifyOrder {
                authApi.getAccessToken()
                tokenRepository.clearToken()
                tokenRepository.saveToken(mockTokenResponse)
            }
        }

    @Test
    fun `fetchAndSaveToken - GIVEN API returns Error - THEN returns Error and does NOT call the DB`() =
        runTest {
            // ARRANGE
            val apiErrorResult = ResultWrapper.Error("Unauthorized", 401)
            coEvery { authApi.getAccessToken() } returns apiErrorResult

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("O resultado deveria ser Error", result is ResultWrapper.Error)
            assertEquals(401, (result as ResultWrapper.Error).code)
            coVerify(exactly = 0) { tokenRepository.clearToken() }
            coVerify(exactly = 0) { tokenRepository.saveToken(any()) }
        }

    @Test
    fun `fetchAndSaveToken - GIVEN API returns Exception - THEN returns Exception and does NOT call the DB`() =
        runTest {
            // ARRANGE
            val apiException = IOException("No Network")
            val apiExceptionResult = ResultWrapper.Exception(apiException)
            coEvery { authApi.getAccessToken() } returns apiExceptionResult

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("O resultado deveria ser Exception", result is ResultWrapper.Exception)
            assertEquals(apiException, (result as ResultWrapper.Exception).exception)

            coVerify(exactly = 0) { tokenRepository.clearToken() }
            coVerify(exactly = 0) { tokenRepository.saveToken(any()) }
        }

    @Test
    fun `fetchAndSaveToken - API success given but database fails - then returns exception`() =
        runTest {
            // ARRANGE
            val dbException = SQLiteException("Database is read-only")
            coEvery { authApi.getAccessToken() } returns apiSuccessResult
            coEvery { tokenRepository.clearToken() } throws dbException

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("O resultado deveria ser Exception", result is ResultWrapper.Exception)
            assertEquals(dbException, (result as ResultWrapper.Exception).exception)
            coVerifyOrder {
                authApi.getAccessToken()
                tokenRepository.clearToken()
            }
            coVerify(exactly = 0) { tokenRepository.saveToken(any()) }
        }
}