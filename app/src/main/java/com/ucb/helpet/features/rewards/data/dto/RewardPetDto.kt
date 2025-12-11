package com.ucb.helpet.features.rewards.data.dto

// Data Transfer Object for pets with rewards
data class RewardPetDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val lostDate: String = "",
    val reward: Int = 0,
    val imageUrl: String = ""
)