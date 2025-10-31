package com.giovanna.amatucci.foodbook.data.local.db

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.giovanna.amatucci.foodbook.data.local.ds.KeyStorage
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManager(private val keyStorage: KeyStorage) {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getOrCreateSecretKey(): SecretKey {
        keyStore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }
        val keyGenSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setKeySize(256).build()

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
        )
        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }

    private fun encrypt(data: ByteArray, secretKey: SecretKey): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data)
        return Pair(encryptedBytes, cipher.iv)
    }

    private fun decrypt(encryptedData: ByteArray, iv: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        return cipher.doFinal(encryptedData)
    }

    suspend fun getDecryptedDbKey(): ByteArray {
        val masterKey = getOrCreateSecretKey()
        val encryptedKeyAndIvString = keyStorage.getEncryptedKey()

        return if (encryptedKeyAndIvString == null) {
            val newDbKey = ByteArray(32)
            SecureRandom().nextBytes(newDbKey)

            val (encryptedDbKey, iv) = encrypt(newDbKey, masterKey)
            val combined = Base64.encodeToString(
                iv, Base64.DEFAULT
            ) + DELIMITER + Base64.encodeToString(
                encryptedDbKey, Base64.DEFAULT
            )
            keyStorage.saveEncryptedKey(combined)
            newDbKey
        } else {
            val parts = encryptedKeyAndIvString.split(DELIMITER)
            val iv = Base64.decode(
                parts[0], Base64.DEFAULT
            )
            val encryptedDbKey = Base64.decode(
                parts[1], Base64.DEFAULT
            )

            decrypt(encryptedDbKey, iv, masterKey)
        }
    }

    companion object {
        private const val KEY_ALIAS = "foodbook_db_master_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val DELIMITER = "]"
    }
}