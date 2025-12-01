package com.ucb.helpet.features.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.ui.theme.HelpetTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.ucb.helpet.features.home.presentation.HomeViewModel
import com.ucb.helpet.features.home.presentation.HomeUiState

@RunWith(AndroidJUnit4::class)
class HomeIntegrationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenIsDisplayed() {
        val mockViewModel = mockk<HomeViewModel>()
        val fakeUiState = HomeUiState.Success(emptyList<Pet>())
        every { mockViewModel.uiState } returns MutableStateFlow(fakeUiState)

        composeTestRule.setContent {
            HelpetTheme {
                HomeScreen(navController = rememberNavController(), homeViewModel = mockViewModel)
            }
        }

        composeTestRule.onNodeWithText("Reúne a las mascotas con sus familias").assertIsDisplayed()
    }
}
