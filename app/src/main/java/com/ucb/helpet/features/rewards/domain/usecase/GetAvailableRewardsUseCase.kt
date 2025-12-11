package com.ucb.helpet.features.rewards.domain.usecase

import com.ucb.helpet.features.rewards.domain.repository.RewardsRepository
import com.ucb.helpet.features.rewards.presentation.RewardPet
import com.ucb.helpet.utils.Resource

class GetAvailableRewardsUseCase(private val repository: RewardsRepository) {
    suspend operator fun invoke(): Resource<List<RewardPet>> {
        return repository.getAvailableRewards()
    }
}