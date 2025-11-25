package com.ucb.helpet.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetAllPetsUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val pets: List<Pet>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}


class HomeViewModel(
    private val getAllPetsUseCase: GetAllPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchRecentPets()
    }

    private fun fetchRecentPets() {
        getAllPetsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    // Take the most recent 5 pets for the home screen
                    val recentPets = result.data?.reversed()?.take(5) ?: emptyList()
                    _uiState.value = HomeUiState.Success(recentPets)
                }
                is Resource.Error -> {
                    _uiState.value = HomeUiState.Error(result.message ?: "Unknown error")
                }
                is Resource.Loading, is Resource.Initial -> {
                     _uiState.value = HomeUiState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }
}
