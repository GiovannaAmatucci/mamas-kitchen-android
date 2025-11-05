package com.giovanna.amatucci.foodbook.data.local.db

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.giovanna.amatucci.foodbook.di.util.constants.KeyStoreConstants
import io.ktor.utils.io.charsets.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptographyManager {

    private val keyStore = KeyStore.getInstance(KeyStoreConstants.ANDROID_KEYSTORE).apply {
        load(null)
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = keyStore.getKey(KeyStoreConstants.KEY_STORE_ALIAS, null) as? SecretKey
        return existingKey ?: generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyStoreConstants.ENCRYPTION_ALGORITHM, KeyStoreConstants.ANDROID_KEYSTORE
        )

        val keyGenSpec = KeyGenParameterSpec.Builder(
            KeyStoreConstants.KEY_STORE_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyStoreConstants.BLOCK_MODE)
            .setEncryptionPaddings(KeyStoreConstants.PADDING).setKeySize(256)
            .setUserAuthenticationRequired(false).build()

        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(data: String?): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(KeyStoreConstants.TRANSFORMATION)
        val secretKey = getOrCreateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data?.toByteArray(Charset.defaultCharset()))

        return Pair(iv, encryptedData)
    }

    fun decrypt(iv: ByteArray, encryptedData: ByteArray): String {
        val cipher = Cipher.getInstance(KeyStoreConstants.TRANSFORMATION)
        val secretKey = getOrCreateSecretKey()

        val spec = GCMParameterSpec(KeyStoreConstants.GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryptedData = cipher.doFinal(encryptedData)

        return String(decryptedData, Charset.defaultCharset())
    }
}