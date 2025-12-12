package com.ucb.helpet.features.home.data.mapper

import com.ucb.helpet.features.home.data.dto.PetDto
import com.ucb.helpet.features.home.domain.model.Pet

fun Pet.toDto(): PetDto {
    return PetDto(
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
