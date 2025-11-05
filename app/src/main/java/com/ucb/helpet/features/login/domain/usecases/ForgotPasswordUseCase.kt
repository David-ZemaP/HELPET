package com.ucb.helpet.features.login.domain.usecases

import com.ucb.helpet.features.login.domain.repository.LoginRepository

class ForgotPasswordUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(email: String) {
        repository.forgotPassword(email)
    }
}
