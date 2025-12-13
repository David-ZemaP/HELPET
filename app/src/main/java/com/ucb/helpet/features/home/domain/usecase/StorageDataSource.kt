package com.ucb.helpet.features.home.domain.usecase

import android.net.Uri

/**
 * Interface for handling data operations related to cloud storage.
 */
interface StorageDataSource {
    /**
     * Uploads an image to cloud storage and returns its public URL.
     * @param imageUri The local URI of the image to upload.
     * @return The public URL of the uploaded image as a String.
     */
    suspend fun uploadImage(imageUri: Uri): String
}
