package com.ucb.helpet.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
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
            when (val result = getUserProfileUseCase()) {
                is Resource.Success -> {
                    _uiState.value = ProfileUiState.Success(result.data)
                }
                is Resource.Error -> {
                    _uiState.value = ProfileUiState.Error(result.message)
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
