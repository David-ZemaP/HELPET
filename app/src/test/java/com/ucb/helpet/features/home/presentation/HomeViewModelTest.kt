package com.ucb.helpet.features.home.presentation

import app.cash.turbine.test
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetAllPetsUseCase
import com.ucb.helpet.utils.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var getAllPetsUseCase: GetAllPetsUseCase
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllPetsUseCase = mockk(relaxed = true)
        remoteConfig = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRecentPets success state`() = runTest {
        // Given
        val pets = listOf(
            Pet(id = "1", name = "Pet 1"),
            Pet(id = "2", name = "Pet 2"),
            Pet(id = "3", name = "Pet 3"),
            Pet(id = "4", name = "Pet 4"),
            Pet(id = "5", name = "Pet 5"),
            Pet(id = "6", name = "Pet 6")
        )
        every { getAllPetsUseCase() } returns flowOf(Resource.Success(pets))

        // When
        viewModel = HomeViewModel(getAllPetsUseCase, remoteConfig)
        viewModel.uiState.test {
            // Then
            assertEquals(HomeUiState.Loading, awaitItem())
            val successState = awaitItem() as HomeUiState.Success
            assertEquals(5, successState.pets.size)
            assertEquals("Pet 6", successState.pets[0].name) // Reversed and taken 5
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchRecentPets error state`() = runTest {
        // Given
        val errorMessage = "An error occurred"
        every { getAllPetsUseCase() } returns flowOf(Resource.Error(errorMessage))

        // When
        viewModel = HomeViewModel(getAllPetsUseCase, remoteConfig)
        viewModel.uiState.test {
            // Then
            assertEquals(HomeUiState.Loading, awaitItem())
            val errorState = awaitItem() as HomeUiState.Error
            assertEquals(errorMessage, errorState.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchRecentPets loading state`() = runTest {
        // Given
        every { getAllPetsUseCase() } returns flowOf(Resource.Loading)

        // When
        viewModel = HomeViewModel(getAllPetsUseCase, remoteConfig)
        viewModel.uiState.test {
            // Then
            assertEquals(HomeUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
