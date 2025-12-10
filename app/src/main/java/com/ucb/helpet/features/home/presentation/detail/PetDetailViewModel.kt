package com.ucb.helpet.features.home.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetPetByIdUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PetDetailUiState {
    object Loading : PetDetailUiState()
    data class Success(val pet: Pet) : PetDetailUiState()
    data class Error(val message: String) : PetDetailUiState()
}

class PetDetailViewModel(
    private val getPetByIdUseCase: GetPetByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PetDetailUiState>(PetDetailUiState.Loading)
    val uiState: StateFlow<PetDetailUiState> = _uiState

    fun loadPet(petId: String) {
        viewModelScope.launch {
            _uiState.value = PetDetailUiState.Loading
            when (val result = getPetByIdUseCase(petId)) {
                is Resource.Success -> _uiState.value = PetDetailUiState.Success(result.data)
                is Resource.Error -> _uiState.value = PetDetailUiState.Error(result.message)
                else -> {}
            }
        }
    }
}