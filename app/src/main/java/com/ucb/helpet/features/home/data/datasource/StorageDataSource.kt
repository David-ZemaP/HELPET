package com.ucb.helpet.features.home.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageDataSource {

    private val storageRef = FirebaseStorage.getInstance().reference.child("pet_images")

    suspend fun uploadPetImage(imageUri: Uri): String {
        val imageId = UUID.randomUUID().toString()
        val fileRef = storageRef.child(imageId)
        
        return try {
            fileRef.putFile(imageUri).await()
            val downloadUrl = fileRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            throw Exception("Error al subir la imagen: ${e.message}")
        }
    }
}
