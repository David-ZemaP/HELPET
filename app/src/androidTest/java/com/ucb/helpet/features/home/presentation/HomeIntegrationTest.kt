package com.ucb.helpet.features.home.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ucb.helpet.di.remoteConfigModule
import com.ucb.helpet.ui.theme.HelpetTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class HomeIntegrationTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel by inject<HomeViewModel>()

    @Before
    fun setUp() {
        loadKoinModules(remoteConfigModule)
    }

    @After
    fun tearDown() {
        unloadKoinModules(remoteConfigModule)
    }

    @Test
    fun testHomeScreenIsDisplayed() {
        composeTestRule.setContent {
            HelpetTheme {
                HomeScreen(navController = rememberNavController(), homeViewModel = viewModel)
            }
        }

        // Wait until the default title from Remote Config is displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Welcome to Helpet!")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Now that we've waited, we can assert that it is displayed.
        composeTestRule.onNodeWithText("Welcome to Helpet!").assertIsDisplayed()
    }
}
