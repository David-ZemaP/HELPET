package com.ucb.helpet.features.home.domain.usecase

import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore
import com.ucb.helpet.utils.Resource

class ReportPetUseCase(
    private val repository: PetRepository,
    private val dataStore: IRepositoryDataStore
) {
    suspend operator fun invoke(
        name: String,
        type: String,
        location: String,
        description: String,
        status: String
    ): Resource<Unit> {
        val ownerId = dataStore.getToken()


        val placeholderImage = "https://i.imgur.com/8zQ2X9C.png"

        val pet = Pet(
            name = name,
            type = type,
            location = location,
            description = description,
            status = status,
            imageUrl = placeholderImage,
            ownerId = ownerId
        )
        return repository.reportPet(pet)
    }
}