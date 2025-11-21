package com.ucb.helpet.features.home.domain.model

data class Pet(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val location: String = "",
    val description: String = "",
    val status: String = "Perdido",
    val imageUrl: String = "",
    val ownerId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)