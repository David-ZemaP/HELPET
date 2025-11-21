package com.ucb.helpet.features.home.presentation.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.home.domain.usecase.ReportPetUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ReportPetUiState {
    object Idle : ReportPetUiState()
    object Loading : ReportPetUiState()
    object Success : ReportPetUiState()
    data class Error(val message: String) : ReportPetUiState()
}

class ReportPetViewModel(
    private val reportPetUseCase: ReportPetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReportPetUiState>(ReportPetUiState.Idle)
    val uiState: StateFlow<ReportPetUiState> = _uiState

    fun reportPet(name: String, type: String, location: String, description: String, status: String) {
        viewModelScope.launch {
            _uiState.value = ReportPetUiState.Loading

            if (name.isBlank() || location.isBlank() || type.isBlank()) {
                _uiState.value = ReportPetUiState.Error("Por favor completa los campos obligatorios")
                return@launch
            }

            val result = reportPetUseCase(name, type, location, description, status)
            when (result) {
                is Resource.Success -> _uiState.value = ReportPetUiState.Success
                is Resource.Error -> _uiState.value = ReportPetUiState.Error(result.message)
                else -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = ReportPetUiState.Idle
    }
}