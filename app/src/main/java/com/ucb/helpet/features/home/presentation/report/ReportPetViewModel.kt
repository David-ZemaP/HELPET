package com.ucb.helpet.features.home.presentation.report

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.R
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.ReportPetUseCase
import com.ucb.helpet.features.notifications.NotificationHelper
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
    application: Application,
    private val reportPetUseCase: ReportPetUseCase,
    private val notificationHelper: NotificationHelper,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ReportPetUiState>(ReportPetUiState.Idle)
    val uiState: StateFlow<ReportPetUiState> = _uiState

    fun reportPet(pet: Pet, imageUri: Uri?) {
        viewModelScope.launch {
            _uiState.value = ReportPetUiState.Loading

            if (pet.name.isBlank() || pet.type.isBlank() || pet.location.isBlank()) {
                _uiState.value = ReportPetUiState.Error(getApplication<Application>().getString(R.string.report_pet_error_mandatory_fields))
                return@launch
            }

            when (val result = reportPetUseCase(pet, imageUri)) {
                is Resource.Success -> {
                    _uiState.value = ReportPetUiState.Success
                    notificationHelper.showPetPublishedNotification(pet.name)
                }
                is Resource.Error -> _uiState.value = ReportPetUiState.Error(result.message)
                else -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = ReportPetUiState.Idle
    }
}