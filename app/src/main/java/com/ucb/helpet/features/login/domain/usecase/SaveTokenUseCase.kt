package com.ucb.helpet.features.login.domain.usecase

import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore

class SaveTokenUseCase(
    val repository: IRepositoryDataStore
) {
    suspend fun invoke(token: String) {
        repository.saveToken(token)
    }
}