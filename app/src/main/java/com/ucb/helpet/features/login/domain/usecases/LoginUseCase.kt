package com.ucb.helpet.features.login.domain.usecases

import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.utils.Resource

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        return repository.login(email, password)
    }
}
