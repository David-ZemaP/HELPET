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
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    // Update Success to include list of pets
    data class Success(val user: User, val pets: List<Pet> = emptyList()) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPetsUseCase: GetUserPetsUseCase // Inject new use case
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            // 1. Fetch User
            when (val userResult = getUserProfileUseCase()) {
                is Resource.Success -> {
                    val user = userResult.data

                    // 2. Fetch User's Pets using the userId
                    val petsResult = getUserPetsUseCase(user.userId)
                    val pets = if (petsResult is Resource.Success) petsResult.data else emptyList()

                    _uiState.value = ProfileUiState.Success(user, pets)
                }
                is Resource.Error -> {
                    _uiState.value = ProfileUiState.Error(userResult.message)
                }
                else -> {}
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