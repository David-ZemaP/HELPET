package com.ucb.helpet.features.home.domain.usecase

import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.utils.Resource

class GetPetByIdUseCase(private val repository: PetRepository) {
    suspend operator fun invoke(petId: String): Resource<Pet> {
        return repository.getPetById(petId)
    }
}