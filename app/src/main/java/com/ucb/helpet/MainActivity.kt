package com.ucb.helpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.helpet.features.login.presentation.LoginScreen
import com.ucb.helpet.features.login.presentation.register.RegisterScreen
import com.ucb.helpet.ui.theme.HelpetTheme

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
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onRegisterClick = { navController.navigate("register") })
        }
        composable("register") {
            RegisterScreen()
        }
    }
}
