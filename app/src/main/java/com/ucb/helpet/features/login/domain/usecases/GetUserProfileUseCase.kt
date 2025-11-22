package com.ucb.helpet.features.login.domain.usecases

import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.utils.Resource

class GetUserProfileUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(): Resource<User> {
        return repository.getUserProfile()
    }
}