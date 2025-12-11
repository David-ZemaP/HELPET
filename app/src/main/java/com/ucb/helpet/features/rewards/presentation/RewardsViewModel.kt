package com.ucb.helpet.features.rewards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.rewards.domain.usecase.GetAvailableRewardsUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RewardsViewModel(private val getAvailableRewardsUseCase: GetAvailableRewardsUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<RewardsUiState>(RewardsUiState.Loading)
    val uiState: StateFlow<RewardsUiState> = _uiState

    init {
        loadAvailableRewards()
    }

    private fun loadAvailableRewards() {
        viewModelScope.launch {
            _uiState.value = RewardsUiState.Loading
            when (val result = getAvailableRewardsUseCase()) {
                is Resource.Success -> {
                    _uiState.value = RewardsUiState.Success(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = RewardsUiState.Error(result.message ?: "An unknown error occurred")
                }
                else -> {}
            }
        }
    }
}