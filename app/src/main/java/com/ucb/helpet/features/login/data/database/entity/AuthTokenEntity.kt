
package com.ucb.helpet.features.login.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_token")
data class AuthTokenEntity(
    @PrimaryKey val id: Int = 1,
    val token: String
)
