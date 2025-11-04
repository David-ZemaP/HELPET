package com.ucb.helpet.features.login.domain.repository

import com.ucb.helpet.features.login.presentation.register.UserType
import com.ucb.helpet.utils.Resource

interface LoginRepository {
    suspend fun registerUser(name: String, email: String, password: String, userType: UserType): Resource<Unit>
}
