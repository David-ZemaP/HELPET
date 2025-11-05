package com.ucb.helpet.features.login.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.helpet.features.login.data.database.entity.AuthTokenEntity

@Dao
interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(token: AuthTokenEntity)

    @Query("SELECT * FROM auth_token WHERE id = 1")
    suspend fun getToken(): AuthTokenEntity?

    @Query("DELETE FROM auth_token")
    suspend fun deleteToken()
}
