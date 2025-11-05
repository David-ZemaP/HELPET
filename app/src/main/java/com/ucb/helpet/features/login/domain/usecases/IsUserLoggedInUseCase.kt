package com.ucb.helpet.features.login.domain.usecases

import com.ucb.helpet.features.login.domain.repository.LoginRepository

class IsUserLoggedInUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isUserLoggedIn()
    }
}
