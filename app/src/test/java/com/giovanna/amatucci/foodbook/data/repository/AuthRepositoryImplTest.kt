package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.CryptographyManager
import com.giovanna.amatucci.foodbook.data.local.db.dao.AccessTokenDao
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
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

    @MockK(relaxUnitFun = true)
    private lateinit var accessTokenDao: AccessTokenDao

    @MockK
    private lateinit var cryptoManager: CryptographyManager

    @MockK(relaxUnitFun = true)
    private lateinit var logWriter: LogWriter

    private lateinit var authRepository: AuthRepositoryImpl

    // --- MOCK DATA ---
    private val mockTokenResponse = TokenResponse(
        accessToken = "fake-raw-token",
        tokenType = "Bearer",
        expiresIn = 3600
    )

    private val fakeIv = "fake_iv".toByteArray()
    private val fakeEncryptedToken = "fake_encrypted_data".toByteArray()

    // CORREÇÃO AQUI: Usamos 'to' para criar um Pair, igual no seu TokenRepositoryImplTest
    private val mockEncryptedData = fakeIv to fakeEncryptedToken

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(
            authApi = authApi,
            dao = accessTokenDao, // Agora injetamos o DAO
            cryptoManager = cryptoManager,   // Agora injetamos o Manager
            logWriter = logWriter
        )
    }

    @Test
    fun `fetchAndSaveToken - GIVEN API Success - THEN encrypts token, saves to DAO and returns Success`() =
        runTest {
            // ARRANGE
            val apiSuccessResult = ResultWrapper.Success(mockTokenResponse)

            // 1. API retorna sucesso
            coEvery { authApi.getAccessToken() } returns apiSuccessResult

            // 2. CryptoManager retorna o Pair (iv, data)
            // Nota: Se o método encrypt não for suspend, use 'every'. Se for, use 'coEvery'.
            // Baseado no seu TokenRepositoryImplTest, parece ser 'coEvery'.
            coEvery { cryptoManager.encrypt(any()) } returns mockEncryptedData

            // 3. DAO aceita a gravação
            coEvery { accessTokenDao.saveToken(any()) } returns Unit

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("O resultado deveria ser Success", result is ResultWrapper.Success)
            assertEquals(mockTokenResponse, (result as ResultWrapper.Success).data)

            // Verifica a ordem das chamadas
            coVerifyOrder {
                authApi.getAccessToken()
                cryptoManager.encrypt(any())
                accessTokenDao.saveToken(any())
            }

            // Verifica se o objeto salvo no banco tem os dados criptografados corretos
            val slot = slot<TokenEntity>()
            coVerify { accessTokenDao.saveToken(capture(slot)) }

            val savedEntity = slot.captured
            assertArrayEquals(fakeIv, savedEntity.initializationVector)
            assertArrayEquals(fakeEncryptedToken, savedEntity.encryptedAccessToken)
        }

    @Test
    fun `fetchAndSaveToken - GIVEN API Error - THEN returns Error and DOES NOT call DAO`() =
        runTest {
            // ARRANGE
            val apiErrorResult = ResultWrapper.Error("Unauthorized", 401)
            coEvery { authApi.getAccessToken() } returns apiErrorResult

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue(result is ResultWrapper.Error)
            assertEquals(401, (result as ResultWrapper.Error).code)

            // Garante que não tentou criptografar nem salvar
            coVerify(exactly = 0) { cryptoManager.encrypt(any()) }
            coVerify(exactly = 0) { accessTokenDao.saveToken(any()) }
        }

    @Test
    fun `fetchAndSaveToken - GIVEN API Exception - THEN returns Exception and DOES NOT call DAO`() =
        runTest {
            // ARRANGE
            val apiException = IOException("No Network")
            val apiExceptionResult = ResultWrapper.Exception(apiException)
            coEvery { authApi.getAccessToken() } returns apiExceptionResult

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue(result is ResultWrapper.Exception)
            assertEquals(apiException, (result as ResultWrapper.Exception).exception)

            coVerify(exactly = 0) { cryptoManager.encrypt(any()) }
            coVerify(exactly = 0) { accessTokenDao.saveToken(any()) }
        }

    @Test
    fun `fetchAndSaveToken - GIVEN API Success BUT DB fails - THEN returns Exception`() =
        runTest {
            // ARRANGE
            coEvery { authApi.getAccessToken() } returns ResultWrapper.Success(mockTokenResponse)
            coEvery { cryptoManager.encrypt(any()) } returns mockEncryptedData

            val dbException = RuntimeException("Disk full")
            coEvery { accessTokenDao.saveToken(any()) } throws dbException

            // ACT
            val result = authRepository.fetchAndSaveToken()

            // ASSERT
            assertTrue("Deveria retornar Exception capturada", result is ResultWrapper.Exception)
            assertEquals(dbException, (result as ResultWrapper.Exception).exception)

            coVerify { accessTokenDao.saveToken(any()) }
        }
}