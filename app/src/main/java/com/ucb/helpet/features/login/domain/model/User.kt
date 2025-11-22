package com.ucb.helpet.features.login.domain.model

import com.ucb.helpet.features.login.presentation.register.UserType

data class User(
    val userId: String,
    val name: String,
    val email: Email,
    val phone: Phone? = null,
    val location: Location? = null,
    val userType: UserType = UserType.CLIENT
)