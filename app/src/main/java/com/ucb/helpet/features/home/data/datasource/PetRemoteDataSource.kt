package com.ucb.helpet.features.home.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.ucb.helpet.features.home.domain.model.Pet
import kotlinx.coroutines.tasks.await

class PetRemoteDataSource {

    private val petsRef = FirebaseDatabase.getInstance().getReference("pets")

    suspend fun savePet(pet: Pet) {
        val key = petsRef.push().key ?: throw Exception("No se pudo generar la clave")
        val petWithId = pet.copy(id = key)
        petsRef.child(key).setValue(petWithId).await()
    }

    // NEW FUNCTION
    suspend fun getPetsByOwner(ownerId: String): List<Pet> {
        return try {
            // Query pets where "ownerId" matches the provided ID
            val snapshot = petsRef.orderByChild("ownerId").equalTo(ownerId).get().await()

            // Convert snapshot children to Pet objects
            snapshot.children.mapNotNull { it.getValue(Pet::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getPetById(petId: String): Pet {
        return try {
            val snapshot = petsRef.child(petId).get().await()
            snapshot.getValue(Pet::class.java) ?: throw Exception("Mascota no encontrada")
        } catch (e: Exception) {
            throw Exception("Error al cargar mascota: ${e.message}")
        }
    }
}