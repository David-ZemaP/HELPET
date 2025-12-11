package com.ucb.helpet.features.home.presentation.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.R
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.features.login.domain.repository.LoginRepository
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
    private val repository: PetRepository,
    private val loginRepository: LoginRepository, // Changed from IRepositoryDataStore
    private val notificationHelper: NotificationHelper
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<ReportPetUiState>(ReportPetUiState.Idle)
    val uiState: StateFlow<ReportPetUiState> = _uiState

    fun reportPet(pet: Pet) {
        viewModelScope.launch {
            _uiState.value = ReportPetUiState.Loading

            if (pet.name.isBlank() || pet.type.isBlank() || pet.location.isBlank()) {
                _uiState.value = ReportPetUiState.Error(getApplication<Application>().getString(R.string.report_pet_error_mandatory_fields))
                return@launch
            }

            try {
                // Get the real User ID from the active session
                var ownerId = ""
                val userProfile = loginRepository.getUserProfile()
                if (userProfile is Resource.Success) {
                    ownerId = userProfile.data.userId
                }

                val finalPet = pet.copy(
                    ownerId = ownerId,
                    // Placeholder image until Storage is implemented
                    imageUrl = if(pet.imageUrl.isBlank()) "https://i.imgur.com/8zQ2X9C.png" else pet.imageUrl
                )

                when (val result = repository.reportPet(finalPet)) {
                    is Resource.Success -> {
                        _uiState.value = ReportPetUiState.Success
                        notificationHelper.showPetPublishedNotification(finalPet.name)
                    }
                    is Resource.Error -> _uiState.value = ReportPetUiState.Error(result.message)
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.value = ReportPetUiState.Error(e.message ?: getApplication<Application>().getString(R.string.error_unknown))
            }
        }
    }

    fun resetState() {
        _uiState.value = ReportPetUiState.Idle
    }
}