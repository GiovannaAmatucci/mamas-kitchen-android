package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.AccessTokenDao
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.CryptographyManager
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TokenRepositoryImplTest {

    @MockK(relaxed = true)
    lateinit var dao: AccessTokenDao

    @MockK
    lateinit var cryptoManager: CryptographyManager

    @MockK(relaxed = true)
    lateinit var logWriter: LogWriter

    private lateinit var repository: TokenRepository
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        repository = TokenRepositoryImpl(dao, cryptoManager, logWriter)
    }

    @Test
    fun `saveToken should encrypt and save token entity when access token is not null`() = runTest {
        val fakeToken = "token123"
        val fakeExpiresIn = 3600
        val response = TokenResponse(accessToken = fakeToken, expiresIn = fakeExpiresIn)

        val fakeIv = "iv".toByteArray()
        val fakeEncryptedToken = "encrypted".toByteArray()
        val tokenSlot = slot<TokenEntity>()

        coEvery { cryptoManager.encrypt(fakeToken) } returns (fakeIv to fakeEncryptedToken)
        coEvery { dao.saveToken(capture(tokenSlot)) } just Runs

        val startTime = System.currentTimeMillis()
        repository.saveToken(response)

        coVerify(exactly = 1) { cryptoManager.encrypt(fakeToken) }
        coVerify(exactly = 1) { dao.saveToken(any()) }

        val capturedToken = tokenSlot.captured
        assertEquals(fakeIv, capturedToken.initializationVector)
        assertEquals(fakeEncryptedToken, capturedToken.encryptedAccessToken)
        assertNotNull(capturedToken.expiresAtMillis)
        val expectedExpiresAt = startTime + fakeExpiresIn
        assertTrue(capturedToken.expiresAtMillis >= expectedExpiresAt)
    }

    @Test
    fun `saveToken should do nothing when access token is null`() = runTest {
        val response = TokenResponse(accessToken = null, expiresIn = 3600)

        repository.saveToken(response)

        coVerify(exactly = 0) { cryptoManager.encrypt(any()) }
        coVerify(exactly = 0) { dao.saveToken(any()) }
    }

    @Test
    fun `saveToken should log error when encryption fails`() = runTest {
        val response = TokenResponse(accessToken = "token123", expiresIn = 3600)
        val exception = RuntimeException("Crypto error")

        coEvery { cryptoManager.encrypt(any()) } throws exception

        repository.saveToken(response)

        coVerify(exactly = 1) { cryptoManager.encrypt("token123") }
        coVerify(exactly = 0) { dao.saveToken(any()) }
        coVerify(exactly = 1) { logWriter.e(any(), any(), any()) }
    }

    @Test
    fun `getValidAccessToken SHOULD return decrypted token WHEN token is valid and not expired`() =
        runTest {
            val fakeIv = "iv".toByteArray()
            val fakeEncryptedToken = "encrypted".toByteArray()
            val fakeDecryptedToken = "decrypted_token_123"
            val futureExpiry = System.currentTimeMillis() + 100000

            val entity = TokenEntity(
                encryptedAccessToken = fakeEncryptedToken,
                initializationVector = fakeIv,
                expiresAtMillis = futureExpiry
            )

            coEvery { dao.getToken() } returns entity
            coEvery { cryptoManager.decrypt(fakeIv, fakeEncryptedToken) } returns fakeDecryptedToken

            val result = repository.getValidAccessToken()

            assertEquals(fakeDecryptedToken, result)
            coVerify(exactly = 1) { dao.getToken() }
            coVerify(exactly = 1) { cryptoManager.decrypt(fakeIv, fakeEncryptedToken) }
        }

    @Test
    fun `getValidAccessToken SHOULD return null WHEN token is not found in dao`() = runTest {
        coEvery { dao.getToken() } returns null

        val result = repository.getValidAccessToken()

        assertNull(result)
        coVerify(exactly = 1) { dao.getToken() }
        coVerify(exactly = 0) { cryptoManager.decrypt(any(), any()) }
        coVerify(exactly = 1) { logWriter.w(any(), LogMessages.TOKEN_REPO_GET_NOT_FOUND) }
    }

    @Test
    fun `getValidAccessToken SHOULD return null WHEN token is expired`() = runTest {
        val pastExpiry = System.currentTimeMillis() - 1000 // Expirou 1 seg atrás
        val entity = TokenEntity(
            encryptedAccessToken = "encrypted".toByteArray(),
            initializationVector = "iv".toByteArray(),
            expiresAtMillis = pastExpiry
        )

        coEvery { dao.getToken() } returns entity

        val result = repository.getValidAccessToken()

        assertNull(result)
        coVerify(exactly = 1) { dao.getToken() }
        coVerify(exactly = 0) { cryptoManager.decrypt(any(), any()) }
        coVerify(exactly = 1) { logWriter.w(any(), LogMessages.TOKEN_REPO_GET_EXPIRED) }
    }

    @Test
    fun `getValidAccessToken SHOULD return null and log error WHEN decryption fails`() = runTest {
        val fakeIv = "iv".toByteArray()
        val fakeEncryptedToken = "encrypted".toByteArray()
        val futureExpiry = System.currentTimeMillis() + 100000
        val entity = TokenEntity(
            encryptedAccessToken = fakeEncryptedToken,
            initializationVector = fakeIv,
            expiresAtMillis = futureExpiry
        )
        val exception = RuntimeException("Decrypt error")

        coEvery { dao.getToken() } returns entity
        coEvery { cryptoManager.decrypt(fakeIv, fakeEncryptedToken) } throws exception

        val result = repository.getValidAccessToken()

        assertNull(result)
        coVerify(exactly = 1) { dao.getToken() }
        coVerify(exactly = 1) { cryptoManager.decrypt(fakeIv, fakeEncryptedToken) }
        coVerify(exactly = 1) { logWriter.e(any(), any(), isNull()) }
    }

    @Test
    fun `clearToken SHOULD call dao deleteToken`() = runTest {
        coEvery { dao.deleteToken() } just Runs

        repository.clearToken()

        coVerify(exactly = 1) { dao.deleteToken() }
        coVerify(exactly = 1) { logWriter.w(any(), LogMessages.TOKEN_REPO_CLEAR) }
    }

    @Test
    fun `clearToken SHOULD log error WHEN dao deleteToken fails`() = runTest {
        val exception = RuntimeException("DB error")
        coEvery { dao.deleteToken() } throws exception

        repository.clearToken()
        coVerify(exactly = 1) { dao.deleteToken() }
        coVerify(exactly = 1) { logWriter.e(any(), any(), eq(exception)) }
    }

    @Test
    fun `saveToken SHOULD log error WHEN dao saveToken fails`() = runTest {
        val fakeToken = "token123"
        val fakeExpiresIn = 3600
        val response = TokenResponse(accessToken = fakeToken, expiresIn = fakeExpiresIn)
        val exception = RuntimeException("DAO Save Error")

        val fakeIv = "iv".toByteArray()
        val fakeEncryptedToken = "encrypted".toByteArray()

        coEvery { cryptoManager.encrypt(fakeToken) } returns (fakeIv to fakeEncryptedToken)
        coEvery { dao.saveToken(any()) } throws exception

        repository.saveToken(response)
        coVerify(exactly = 1) { logWriter.e(any(), any(), eq(exception)) }
    }

    @Test
    fun `getValidAccessToken SHOULD throw exception WHEN dao getToken fails`() = runTest {
        val exception = RuntimeException("DAO Get Error")
        coEvery { dao.getToken() } throws exception

        var caughtException: Throwable? = null
        try {
            repository.getValidAccessToken()
        } catch (e: Throwable) {
            caughtException = e
        }
        assertNotNull(caughtException)
        assertTrue(caughtException is RuntimeException)
        assertEquals(exception.message, caughtException?.message)
    }
}