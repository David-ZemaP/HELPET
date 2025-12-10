package com.ucb.helpet.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetAllPetsUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchViewModel(private val getAllPetsUseCase: GetAllPetsUseCase) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets

    private var cachedPets: List<Pet> = emptyList()

    init {
        // Start listening to the real-time pet stream from Firebase
        loadInitialPets()
    }

    private fun loadInitialPets() {
        getAllPetsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    cachedPets = result.data ?: emptyList()
                    _pets.value = cachedPets // Initially, show all pets
                }
                is Resource.Error -> {
                    // Handle error, maybe show a toast or log
                }
                is Resource.Loading -> {
                    // You could show a loading indicator here
                }
                is Resource.Initial -> {
                    // Initial state, do nothing or prepare UI
                }
            }
        }.launchIn(viewModelScope)
    }

    // This function just updates the search query state as you type
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchPets()
    }

    // This function is now public and performs the search on the cached Firebase data
    fun searchPets() {
        val query = searchQuery.value
        _pets.value = if (query.isBlank()) {
            cachedPets // If search is empty, show all pets
        } else {
            cachedPets.filter {
                it.name.contains(query, ignoreCase = true) ||
                (it.breed?.contains(query, ignoreCase = true) == true) ||
                it.location.contains(query, ignoreCase = true)
            }
        }
    }
}
