package com.ucb.helpet.features.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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
    private val getAllPetsUseCase: GetAllPetsUseCase,
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _homeScreenTitle = mutableStateOf("")
    val homeScreenTitle: State<String> = _homeScreenTitle

    init {
        fetchRecentPets()
        fetchAndActivateRemoteConfig()
    }

    private fun fetchAndActivateRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    // Remote Config updated
                }
                // Update title, even if fetch fails (uses defaults)
                _homeScreenTitle.value = remoteConfig.getString("home_screen_title")
            }
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
