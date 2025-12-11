package com.ucb.helpet.features.rewards.presentation

sealed class RewardsUiState {
    object Loading : RewardsUiState()
    data class Success(val rewards: List<RewardPet>) : RewardsUiState()
    data class Error(val message: String) : RewardsUiState()
}