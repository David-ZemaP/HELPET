
package com.ucb.helpet.features.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.usecase.GetAllPetsUseCase
import com.ucb.helpet.ui.theme.HelpetTheme
import com.ucb.helpet.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val getAllPetsUseCase: GetAllPetsUseCase = mockk()

    @Test
    fun homeScreen_displaysSuccessState() {
        // Given
        val pets = listOf(Pet(id = "1", name = "Buddy", description = "A friendly dog", type = "Dog", status = "Lost", location = "La Paz", imageUrl = ""))
        coEvery { getAllPetsUseCase() } returns flowOf(Resource.Success(pets))
        val viewModel = HomeViewModel(getAllPetsUseCase)

        // When
        composeTestRule.setContent {
            HelpetTheme {
                HomeScreen(navController = rememberNavController(), homeViewModel = viewModel)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Buddy").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysErrorState() {
        // Given
        val errorMessage = "Failed to load pets"
        coEvery { getAllPetsUseCase() } returns flowOf(Resource.Error(errorMessage))
        val viewModel = HomeViewModel(getAllPetsUseCase)

        // When
        composeTestRule.setContent {
            HelpetTheme {
                HomeScreen(navController = rememberNavController(), homeViewModel = viewModel)
            }
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).performScrollTo().assertIsDisplayed()
    }
}
