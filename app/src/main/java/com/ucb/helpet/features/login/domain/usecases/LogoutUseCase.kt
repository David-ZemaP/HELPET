package com.ucb.helpet.features.login.domain.usecases

import com.ucb.helpet.features.login.domain.repository.LoginRepository

class LogoutUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
