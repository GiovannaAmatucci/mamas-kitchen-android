package com.giovanna.amatucci.foodbook.data.local.db

import com.giovanna.amatucci.foodbook.util.constants.KeyStoreConstants
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.security.KeyStore

@RunWith(JUnit4::class)
class CryptographyManagerTest {

    private lateinit var cryptographyManager: CryptographyManager

    @Before
    fun setUp() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        keyStore.deleteEntry(KeyStoreConstants.KEY_STORE_ALIAS)

        cryptographyManager = CryptographyManager()
    }

    @Test
    fun encryptionAndDecryption_shouldReturnOriginalText() {
        // ARRANGE
        val originalData = "MySecret123"

        // ACT
        val (iv, encryptedData) = cryptographyManager.encrypt(originalData)

        // ASSERT
        Assert.assertNotEquals(originalData, String(encryptedData, Charsets.UTF_8))
        Assert.assertNotEquals(0, iv.size)
        Assert.assertNotEquals(0, encryptedData.size)
        // ACT
        val decryptedData = cryptographyManager.decrypt(iv, encryptedData)

        // ASSERT FINAL
        Assert.assertEquals(originalData, decryptedData)
    }

    @Test
    fun encryption_shouldProduceDifferentCiphertext_forSameInput() {
        // ARRANGE
        val text = "TextoFixo"

        // ACT
        val (iv1, result1) = cryptographyManager.encrypt(text)
        val (iv2, result2) = cryptographyManager.encrypt(text)

        // ASSERT
        Assert.assertNotEquals(
            "Os vetores de inicialização (IV) devem ser diferentes a cada chamada",
            iv1.contentToString(),
            iv2.contentToString()
        )
        Assert.assertNotEquals(
            "O texto cifrado deve ser diferente devido ao IV único",
            result1.contentToString(),
            result2.contentToString()
        )
    }
}