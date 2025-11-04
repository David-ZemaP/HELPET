package com.ucb.helpet.features.login.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.helpet.features.login.domain.usecase.RegisterUserUseCase
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val userType = MutableStateFlow(UserType.CLIENT)

    private val _registerState = MutableStateFlow<Resource<Unit>>(Resource.Initial)
    val registerState: StateFlow<Resource<Unit>> = _registerState.asStateFlow()

    fun onRegisterClicked() {
        viewModelScope.launch {
            _registerState.value = Resource.Loading
            val result = registerUserUseCase.invoke(
                name.value,
                email.value,
                password.value,
                userType.value
            )
            _registerState.value = result
        }
    }
}
