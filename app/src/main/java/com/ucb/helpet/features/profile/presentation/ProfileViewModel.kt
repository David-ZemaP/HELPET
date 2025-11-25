package com.ucb.helpet.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetUserPetsUseCase
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.domain.usecases.GetUserProfileUseCase
import com.ucb.helpet.features.login.domain.usecases.LogoutUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User, val pets: List<Pet> = emptyList()) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPetsUseCase: GetUserPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        fetchUserProfileAndPets()
    }

    private fun fetchUserProfileAndPets() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            // 1. Fetch User Profile
            when (val userResult = getUserProfileUseCase()) {
                is Resource.Success -> {
                    val user = userResult.data
                    // Set initial success state with user info but empty pet list
                    _uiState.value = ProfileUiState.Success(user)

                    // 2. Start collecting the pet stream using the user's ID
                    getUserPetsUseCase(user.userId).onEach { petsResult ->
                        when (petsResult) {
                            is Resource.Success -> {
                                // When new pet data arrives, update the state
                                val currentState = _uiState.value
                                if (currentState is ProfileUiState.Success) {
                                    _uiState.value = currentState.copy(pets = petsResult.data ?: emptyList())
                                }
                            }
                            is Resource.Error -> {
                                // You might want to show a non-fatal error for the pet list
                                val currentState = _uiState.value
                                if (currentState is ProfileUiState.Success) {
                                     // Optionally, you can show a message or just log it
                                }
                            }
                            // Handle other states if necessary
                            else -> {}
                        }
                    }.launchIn(viewModelScope)
                }
                is Resource.Error -> {
                    _uiState.value = ProfileUiState.Error(userResult.message)
                }
                else -> {
                    // Handle loading/initial for user profile if needed
                }
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.emit(Unit)
        }
    }
}