package com.ucb.helpet.features.rewards.data.dto

import com.ucb.helpet.features.rewards.presentation.RewardPet

fun RewardPetDto.toDomain(): RewardPet {
    return RewardPet(
        name = this.name,
        description = this.description,
        location = this.location,
        lostTime = this.lostDate, // Map lostDate to lostTime
        rewardAmount = this.reward,
        imageUrl = this.imageUrl
    )
}