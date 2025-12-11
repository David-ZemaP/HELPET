package com.ucb.helpet.features.login.data.repository

import com.ucb.helpet.features.login.data.database.dao.AuthTokenDao
import com.ucb.helpet.features.login.data.datasource.LoginRemoteDataSource
import com.ucb.helpet.features.login.data.mapper.AuthTokenMapper
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.presentation.register.UserType
import com.ucb.helpet.utils.Resource

class LoginRepositoryImpl(
    private val remoteDataSource: LoginRemoteDataSource,
    private val authTokenDao: AuthTokenDao
) : LoginRepository {

    override suspend fun registerUser(name: String, email: String, password: String, userType: UserType, phone: String, location: String): Resource<Unit> {
        return try {
            remoteDataSource.register(name, email, password, userType, phone, location)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }

    override suspend fun forgotPassword(email: String) {
        try {
            remoteDataSource.forgotPassword(email)
        } catch (e: Exception) {
            // Propagate exception if necessary, or just log/handle
            println("Forgot Password Error: ${e.message}")
            throw e
        }
    }

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            val loginResponse = remoteDataSource.login(email, password)
            val authTokenEntity = AuthTokenMapper.toEntity(loginResponse.token)
            authTokenDao.saveToken(authTokenEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return remoteDataSource.isFirebaseUserLoggedIn() && authTokenDao.getToken() != null
    }

    override suspend fun logout() {
        remoteDataSource.logout()
        authTokenDao.deleteToken()
    }

    override suspend fun getUserProfile(): Resource<User> {
        return try {
            val currentUserId = remoteDataSource.getCurrentUserId()
            if (currentUserId != null) {
                val user = remoteDataSource.getUser(currentUserId)
                Resource.Success(user)
            } else {
                Resource.Error("No hay sesión activa")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al cargar perfil")
        }
    }
}
