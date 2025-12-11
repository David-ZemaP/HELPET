package com.ucb.helpet.features.rewards.data.repository

import com.ucb.helpet.features.home.data.datasource.PetRemoteDataSource
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.rewards.data.dto.toDomain
import com.ucb.helpet.features.rewards.domain.repository.RewardsRepository
import com.ucb.helpet.features.rewards.presentation.RewardPet
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.first

class RewardsRepositoryImpl(private val petRemoteDataSource: PetRemoteDataSource) : RewardsRepository {

    override suspend fun getAvailableRewards(): Resource<List<RewardPet>> {
        return try {
            val allPets = petRemoteDataSource.getAllPets().first()
            val rewardPets = allPets.filter { it.hasReward }.map { it.toRewardPet() }
            Resource.Success(rewardPets)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}

private fun Pet.toRewardPet(): RewardPet {
    return RewardPet(
        name = this.name,
        description = this.description,
        location = this.location,
        lostTime = this.lastSeenDate, // Assuming lastSeenDate is the equivalent of lostTime
        rewardAmount = 1000, // The Pet model does not have a reward amount, so we use a placeholder
        imageUrl = this.imageUrl
    )
}
