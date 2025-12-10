package com.ucb.helpet.features.home.domain.usecase

import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetUserPetsUseCase(private val repository: PetRepository) {
    operator fun invoke(ownerId: String): Flow<Resource<List<Pet>>> {
        return repository.getPetsByOwner(ownerId)
    }
}