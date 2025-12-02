package com.giovanna.amatucci.foodbook.util.constants

import android.security.keystore.KeyProperties

object KeyStoreConstants {
    const val KEY_STORE_ALIAS = "foodbook_keystore"
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    const val TRANSFORMATION = "$ENCRYPTION_ALGORITHM/$BLOCK_MODE/$PADDING"
    const val GCM_TAG_LENGTH_BITS = 128
}