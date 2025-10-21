package com.ucb.helpet.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.login.domain.usecase.GetTokenUseCase
import com.ucb.helpet.features.login.domain.usecase.SaveTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val saveTokenUseCase: SaveTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                // Simulate a network call
                kotlinx.coroutines.delay(1000)
                val fakeToken = "fake-token-for-$email"
                saveTokenUseCase.invoke(fakeToken)
                _uiState.value = LoginUiState.Success(fakeToken)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}