package com.ucb.helpet.features.home.data.dto

import com.ucb.helpet.features.home.domain.model.Pet

data class PetDto(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val breed: String = "",
    val color: String = "",
    val age: String = "",
    val size: String = "",
    val location: String = "",
    val city: String = "",
    val lastSeenDate: String = "",
    val description: String = "",
    val additionalInfo: String = "",
    val status: String = "Perdido",
    val imageUrl: String = "",
    val ownerId: String = "",
    val contactName: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",
    val hasReward: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

fun PetDto.toDomain(): Pet {
    return Pet(
        id = id,
        name = name,
        type = type,
        breed = breed,
        color = color,
        age = age,
        size = size,
        location = location,
        city = city,
        lastSeenDate = lastSeenDate,
        description = description,
        additionalInfo = additionalInfo,
        status = status,
        imageUrl = imageUrl,
        ownerId = ownerId,
        contactName = contactName,
        contactPhone = contactPhone,
        contactEmail = contactEmail,
        hasReward = hasReward,
        timestamp = timestamp
    )
}
