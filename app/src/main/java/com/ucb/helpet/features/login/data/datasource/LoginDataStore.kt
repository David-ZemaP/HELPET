package com.ucb.helpet.features.login.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginDataStore(
    private val context: Context
) : IRepositoryDataStore {
    companion object {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val TOKEN = stringPreferencesKey("token")
    }

    override suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    override suspend fun getEmail(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USER_EMAIL] ?: ""
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    override suspend fun getToken(): String {
        val preferences = context.dataStore.data.first()
        return preferences[TOKEN] ?: ""
    }
}
