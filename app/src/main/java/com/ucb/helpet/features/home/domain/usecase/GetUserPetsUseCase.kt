package com.ucb.helpet.features.home.domain.usecase

import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.utils.Resource

class GetUserPetsUseCase(private val repository: PetRepository) {
    suspend operator fun invoke(ownerId: String): Resource<List<Pet>> {
        return repository.getPetsByOwner(ownerId)
    }
}