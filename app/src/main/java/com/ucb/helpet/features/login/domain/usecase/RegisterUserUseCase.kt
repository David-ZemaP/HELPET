package com.ucb.helpet.features.login.domain.usecase

import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.presentation.register.UserType
import com.ucb.helpet.utils.Resource

class RegisterUserUseCase(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(name: String, email: String, password: String, userType: UserType, phone: String, location: String): Resource<Unit> {
        return loginRepository.registerUser(name, email, password, userType, phone, location)
    }
}
