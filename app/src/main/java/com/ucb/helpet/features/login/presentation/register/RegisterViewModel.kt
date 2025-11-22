package com.ucb.helpet.features.login.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.login.domain.usecase.RegisterUserUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Updated to accept phone and location
    fun register(name: String, email: String, password: String, userType: UserType, phone: String, location: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                _uiState.value = RegisterUiState.Error("Por favor completa todos los campos obligatorios.")
                return@launch
            }
            if (password.length < 8) {
                _uiState.value = RegisterUiState.Error("La contraseña debe tener al menos 8 caracteres.")
                return@launch
            }

            // Pass phone and location
            when (val result = registerUserUseCase(name, email, password, userType, phone, location)) {
                is Resource.Success -> {
                    _uiState.value = RegisterUiState.Success
                }
                is Resource.Error -> {
                    _uiState.value = RegisterUiState.Error(result.message ?: "Ocurrió un error desconocido")
                }
                else -> {}
            }
        }
    }
    fun resetState(){
        _uiState.value = RegisterUiState.Idle
    }
}
