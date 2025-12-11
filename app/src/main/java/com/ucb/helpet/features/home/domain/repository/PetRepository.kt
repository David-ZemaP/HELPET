package com.ucb.helpet.features.home.domain.repository

import com.ucb.helpet.features.home.data.datasource.PetRemoteDataSource
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface PetRepository {
    suspend fun reportPet(pet: Pet): Resource<Unit>
    // NEW
    fun getPetsByOwner(ownerId: String): Flow<Resource<List<Pet>>>
    suspend fun getPetById(petId: String): Resource<Pet>
    fun getAllPets(): Flow<Resource<List<Pet>>>
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

    override fun getPetsByOwner(ownerId: String): Flow<Resource<List<Pet>>> {
        return remoteDataSource.getPetsByOwner(ownerId)
            .map<List<Pet>, Resource<List<Pet>>> { Resource.Success(it) }
            .catch { e -> emit(Resource.Error(e.message ?: "Error al obtener mascotas")) }
    }

    override fun getAllPets(): Flow<Resource<List<Pet>>> {
        return remoteDataSource.getAllPets()
            .map<List<Pet>, Resource<List<Pet>>> { Resource.Success(it) }
            .catch { e -> emit(Resource.Error(e.message ?: "Error al obtener todas las mascotas")) }
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