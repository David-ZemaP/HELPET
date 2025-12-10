package com.ucb.helpet.features.login.presentation

import android.content.IntentSender

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class LaunchGoogleSignIn(val intentSender: IntentSender?) : LoginUiState()
}
