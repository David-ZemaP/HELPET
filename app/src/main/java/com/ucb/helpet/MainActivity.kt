package com.ucb.helpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.helpet.features.home.presentation.HomeScreen
import com.ucb.helpet.features.login.presentation.LoginScreen
import com.ucb.helpet.features.login.presentation.LoginUiState
import com.ucb.helpet.features.login.presentation.LoginViewModel
import com.ucb.helpet.features.login.presentation.forgotpassword.ForgotPasswordScreen
import com.ucb.helpet.features.login.presentation.register.RegisterScreen
import com.ucb.helpet.features.splash.presentation.SplashScreen
import com.ucb.helpet.ui.theme.HelpetTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelpetTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
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
            HomeScreen()
        }
    }
}
