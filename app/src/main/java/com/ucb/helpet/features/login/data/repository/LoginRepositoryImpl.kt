package com.ucb.helpet.features.login.data.repository

import com.ucb.helpet.features.login.data.datasource.LoginRemoteDataSource
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.presentation.register.UserType
import com.ucb.helpet.utils.Resource

class LoginRepositoryImpl(private val loginRemoteDataSource: LoginRemoteDataSource) : LoginRepository {

    override suspend fun registerUser(name: String, email: String, password: String, userType: UserType): Resource<Unit> {
        return try {
            loginRemoteDataSource.register(name, email, password, userType.apiName)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}
