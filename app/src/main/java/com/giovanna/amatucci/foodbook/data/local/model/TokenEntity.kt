package com.giovanna.amatucci.foodbook.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tokenEntity")
data class TokenEntity(
    @PrimaryKey
    val id: String = "default_token",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val encryptedAccessToken: ByteArray,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val initializationVector: ByteArray,
    @ColumnInfo(name = "expires_at_millis")
    val expiresAtMillis: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TokenEntity

        if (id != other.id) return false
        if (!encryptedAccessToken.contentEquals(other.encryptedAccessToken)) return false
        if (!initializationVector.contentEquals(other.initializationVector)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + encryptedAccessToken.contentHashCode()
        result = 31 * result + initializationVector.contentHashCode()
        result = 31 * result + expiresAtMillis.hashCode()
        return result
    }
}