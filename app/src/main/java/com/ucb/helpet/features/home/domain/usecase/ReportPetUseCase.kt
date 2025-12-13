package com.ucb.helpet.features.home.domain.usecase

import android.net.Uri
import com.ucb.helpet.features.home.data.datasource.StorageDataSource
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.utils.Resource
import java.lang.Exception

class ReportPetUseCase(
    private val repository: PetRepository,
    private val loginRepository: LoginRepository,
    private val storageDataSource: StorageDataSource,
) {
    suspend operator fun invoke(pet: Pet, imageUri: Uri?): Resource<Unit> {
        try {
            var imageUrl = if (pet.imageUrl.isBlank()) "https://i.imgur.com/8zQ2X9C.png" else pet.imageUrl

            if (imageUri != null) {
                try {
                    imageUrl = storageDataSource.uploadPetImage(imageUri)
                } catch (e: Exception) {
                    return Resource.Error("Error al subir la imagen: ${e.message}")
                }
            }

            val userProfile = loginRepository.getUserProfile()
            val ownerId = (userProfile as? Resource.Success)?.data?.userId

            if (ownerId.isNullOrBlank()) {
                return Resource.Error("Usuario no autenticado.")
            }

            val finalPet = pet.copy(
                ownerId = ownerId,
                imageUrl = imageUrl
            )
            return repository.reportPet(finalPet)
        } catch (e: Exception) {
            return Resource.Error(e.message ?: "Ocurrió un error desconocido")
        }
    }
}