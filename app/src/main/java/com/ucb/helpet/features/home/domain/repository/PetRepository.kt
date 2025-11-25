package com.ucb.helpet.features.home.domain.repository

import com.ucb.helpet.features.home.data.datasource.PetRemoteDataSource
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.utils.Resource

interface PetRepository {
    suspend fun reportPet(pet: Pet): Resource<Unit>
    // NEW
    suspend fun getPetsByOwner(ownerId: String): Resource<List<Pet>>

    suspend fun getPetById(petId: String): Resource<Pet>
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

    // NEW IMPLEMENTATION
    override suspend fun getPetsByOwner(ownerId: String): Resource<List<Pet>> {
        return try {
            val pets = remoteDataSource.getPetsByOwner(ownerId)
            Resource.Success(pets)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener mascotas")
        }
    }

    override suspend fun getPetById(petId: String): Resource<Pet> {
        return try {
            val pet = remoteDataSource.getPetById(petId)
            Resource.Success(pet)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error desconocido")
        }
    }
}