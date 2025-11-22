package com.ucb.helpet.features.login.domain.repository

import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.presentation.register.UserType
import com.ucb.helpet.utils.Resource

interface LoginRepository {
    suspend fun registerUser(name: String, email: String, password: String, userType: UserType, phone: String, location: String): Resource<Unit>
    suspend fun forgotPassword(email: String)
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun logout()

    suspend fun getUserProfile(): Resource<User>
}
