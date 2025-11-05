package com.ucb.helpet.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.login.domain.usecases.IsUserLoggedInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SplashState {
    object Loading : SplashState()
    object UserLoggedIn : SplashState()
    object UserNotLoggedIn : SplashState()
}

class SplashViewModel(private val isUserLoggedInUseCase: IsUserLoggedInUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        viewModelScope.launch {
            if (isUserLoggedInUseCase()) {
                _uiState.value = SplashState.UserLoggedIn
            } else {
                _uiState.value = SplashState.UserNotLoggedIn
            }
        }
    }
}
