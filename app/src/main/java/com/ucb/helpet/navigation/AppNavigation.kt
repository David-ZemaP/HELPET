package com.ucb.helpet.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.presentation.HomeScreen
import com.ucb.helpet.features.home.presentation.detail.PetDetailScreen
import com.ucb.helpet.features.home.presentation.detail.PetDetailUiState
import com.ucb.helpet.features.home.presentation.detail.PetDetailViewModel
import com.ucb.helpet.features.home.presentation.report.ReportPetScreen
import com.ucb.helpet.features.login.presentation.LoginScreen
import com.ucb.helpet.features.login.presentation.LoginUiState
import com.ucb.helpet.features.login.presentation.LoginViewModel
import com.ucb.helpet.features.login.presentation.forgotpassword.ForgotPasswordScreen
import com.ucb.helpet.features.login.presentation.register.RegisterScreen
import com.ucb.helpet.features.splash.presentation.SplashScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = "splash") {


        composable("splash") {
            SplashScreen(navController)
        }


        composable("login") {

            val viewModel: LoginViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(uiState) {
                if (uiState is LoginUiState.Success) {
                    navController.navigate("home") {

                        popUpTo("login") { inclusive = true }
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                viewModel = viewModel,
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot_password") }
            )
        }


        composable("register") {
            RegisterScreen(navController = navController)
        }


        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackToLoginClick = { navController.popBackStack() }
            )
        }


        composable("home") {
            HomeScreen(navController = navController)
        }


        composable("report_pet") {
            ReportPetScreen(navController = navController)
        }

        composable(
            route = "pet_detail/{petJson}",
            arguments = listOf(navArgument("petJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val petJson = backStackEntry.arguments?.getString("petJson")
            val pet = Gson().fromJson(petJson, Pet::class.java)
            if (pet != null) {
                PetDetailScreen(navController, pet)
            }
        }

        composable(
            route = "pet_detail_id/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: return@composable

            val viewModel: PetDetailViewModel = koinViewModel()
            // Trigger load
            LaunchedEffect(petId) {
                viewModel.loadPet(petId)
            }

            val uiState by viewModel.uiState.collectAsState()

            when (val state = uiState) {
                is PetDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is PetDetailUiState.Success -> {
                    PetDetailScreen(navController, state.pet)
                }

                is PetDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error al cargar mascota")
                    }
                }
            }
        }

        composable(
            route = "pet_detail_id/{petId}",
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: return@composable

            val viewModel: PetDetailViewModel = koinViewModel()

            LaunchedEffect(petId) {
                viewModel.loadPet(petId)
            }

            val uiState by viewModel.uiState.collectAsState()

            when(val state = uiState) {
                is PetDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is PetDetailUiState.Success -> {
                    PetDetailScreen(navController, state.pet)
                }
                is PetDetailUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.message}")
                    }
                }
            }
        }
    }
}