package com.ucb.helpet.features.login.domain.model

import com.ucb.helpet.features.login.presentation.register.UserType

/**
 * Represents a user in the domain layer. This is a clean data class.
 */
data class User(
    val userId: String = "", // This will be the key in Firebase, often derived from email
    val name: String = "",
    val email: String = "",
    val phone: String = "",      // New field
    val location: String = "",   // New field
    val userType: UserType = UserType.CLIENT,
    val password: String = ""
)
