package com.ucb.helpet.features.login.data.repository




import com.ucb.helpet.features.login.data.datasource.LoginDataStore
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore

class RepositoryDataStore(
    val loginDataStore: LoginDataStore
): IRepositoryDataStore {
    override suspend fun saveEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getEmail(): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: String) {
        loginDataStore.saveToken(token)
    }

    override suspend fun getToken(): String {
        return loginDataStore.getToken()
    }
}