package com.ucb.helpet.features.login.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.R
import com.ucb.helpet.features.login.domain.usecases.IsUserLoggedInUseCase
import com.ucb.helpet.features.login.domain.usecases.LoginUseCase
import com.ucb.helpet.features.login.presentation.google.GoogleAuthUiClient
import com.ucb.helpet.features.login.presentation.google.SignInResult
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application,
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase, // It will be used later in MainActivity
    private val googleAuthUiClient: GoogleAuthUiClient
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _uiState.update {
            LoginUiState.Success
        }
    }

    fun onGoogleSignInClicked() {
        viewModelScope.launch {
            val signInIntentSender = googleAuthUiClient.signIn()
            _uiState.update {
                LoginUiState.LaunchGoogleSignIn(signInIntentSender)
            }
        }
    }

    fun signInWithIntent(intent: Intent) {
        viewModelScope.launch {
            val signInResult = googleAuthUiClient.signInWithIntent(intent)
            onSignInResult(signInResult)
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = loginUseCase(email, pass)) {
                is Resource.Success -> {
                    _uiState.value = LoginUiState.Success
                }
                is Resource.Error -> {
                    val errorMessage = result.message ?: getApplication<Application>().getString(R.string.error_unknown)
                    _uiState.value = LoginUiState.Error(errorMessage)
                }
                else -> {}
            }
        }
    }
}