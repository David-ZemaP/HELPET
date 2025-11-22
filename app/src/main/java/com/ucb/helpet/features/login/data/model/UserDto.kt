package com.ucb.helpet.features.login.data.model

import com.ucb.helpet.features.login.presentation.register.UserType

data class UserDto(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val userType: String = "CLIENT" // Storing enum as String is safer for DB
)
