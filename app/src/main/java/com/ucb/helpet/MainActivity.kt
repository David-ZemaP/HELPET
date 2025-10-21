package com.ucb.helpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ucb.helpet.features.login.presentation.LoginScreen
import com.ucb.helpet.ui.theme.HelpetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelpetTheme {
                LoginScreen()
            }
        }
    }
}