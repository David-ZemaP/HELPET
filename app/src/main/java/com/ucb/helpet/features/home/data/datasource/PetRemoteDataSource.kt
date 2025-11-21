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
}