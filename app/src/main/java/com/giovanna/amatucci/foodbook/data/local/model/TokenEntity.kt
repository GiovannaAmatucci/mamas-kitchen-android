package com.giovanna.amatucci.foodbook.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_token")
data class TokenEntity(
    @PrimaryKey val id: Int = 1,
    val accessToken: String
)