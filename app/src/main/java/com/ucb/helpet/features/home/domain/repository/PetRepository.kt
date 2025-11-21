package com.ucb.helpet.features.home.domain.repository

import com.ucb.helpet.features.home.data.datasource.PetRemoteDataSource
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.utils.Resource

interface PetRepository {
    suspend fun reportPet(pet: Pet): Resource<Unit>
}

class PetRepositoryImpl(
    private val remoteDataSource: PetRemoteDataSource
) : PetRepository {
    override suspend fun reportPet(pet: Pet): Resource<Unit> {
        return try {
            remoteDataSource.savePet(pet)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al guardar la mascota")
        }
    }
}