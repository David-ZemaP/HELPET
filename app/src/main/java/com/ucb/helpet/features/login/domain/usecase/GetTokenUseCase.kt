package com.ucb.helpet.features.login.domain.usecase

import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore

class GetTokenUseCase
    (val repository: IRepositoryDataStore){

    suspend fun invoke(): String {
        return repository.getToken()
    }

}