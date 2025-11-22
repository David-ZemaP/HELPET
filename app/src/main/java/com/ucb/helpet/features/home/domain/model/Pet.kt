package com.ucb.helpet.features.home.domain.model

data class Pet(
    val id: String = "",
    val name: String = "",
    val type: String = "", // Perro, Gato
    val breed: String = "", // Raza
    val color: String = "",
    val age: String = "",
    val size: String = "", // Pequeño, Mediano, Grande
    val location: String = "", // Dirección/Zona
    val city: String = "",
    val lastSeenDate: String = "", // Timestamp or String date
    val description: String = "",
    val additionalInfo: String = "",
    val status: String = "Perdido", // Perdido vs Encontrado
    val imageUrl: String = "",
    val ownerId: String = "",

    val contactName: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",
    val hasReward: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)