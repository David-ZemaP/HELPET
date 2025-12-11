package com.ucb.helpet.features.rewards.domain.repository

import com.ucb.helpet.features.rewards.presentation.RewardPet
import com.ucb.helpet.utils.Resource

interface RewardsRepository {
    suspend fun getAvailableRewards(): Resource<List<RewardPet>>
}