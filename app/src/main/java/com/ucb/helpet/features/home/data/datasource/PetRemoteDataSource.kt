package com.ucb.helpet.features.home.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucb.helpet.features.home.domain.model.Pet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PetRemoteDataSource {

    private val petsRef = FirebaseDatabase.getInstance().getReference("pets")

    suspend fun savePet(pet: Pet) {
        val key = petsRef.push().key ?: throw Exception("No se pudo generar la clave")
        val petWithId = pet.copy(id = key)
        petsRef.child(key).setValue(petWithId).await()
    }

    fun getPetsByOwner(ownerId: String): Flow<List<Pet>> = callbackFlow {
        val query = petsRef.orderByChild("ownerId").equalTo(ownerId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pets = snapshot.children.mapNotNull { it.getValue(Pet::class.java) }
                trySend(pets)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)

        awaitClose { query.removeEventListener(listener) }
    }

    fun getAllPets(): Flow<List<Pet>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pets = snapshot.children.mapNotNull { it.getValue(Pet::class.java) }
                trySend(pets)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        petsRef.addValueEventListener(listener)

        awaitClose { petsRef.removeEventListener(listener) }
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

