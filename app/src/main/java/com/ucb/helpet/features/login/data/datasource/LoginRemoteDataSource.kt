package com.ucb.helpet.features.login.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.ucb.helpet.features.login.data.api.FirebaseService
import com.ucb.helpet.features.login.data.model.LoginResponse
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.presentation.register.UserType
import kotlinx.coroutines.tasks.await

class LoginRemoteDataSource(private val firebaseService: FirebaseService) {

    // IMPORTANT: Reference the Realtime Database instance from FirebaseService
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")

    suspend fun register(name: String, email: String, password: String, userType: UserType) {
        try {
            // 1. Authenticate and create user using Firebase Auth
            val authResult = firebaseService.auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al crear el usuario. Intenta de nuevo.")

            // 2. Save auxiliary user data (using the secure Firebase Auth UID as the key)
            val user = User(
                userId = firebaseUser.uid,
                name = name,
                email = email,
                password = "", // Empty password here since Firebase Auth handles it securely
                userType = userType
            )
            usersRef.child(firebaseUser.uid).setValue(user).await()
        } catch (e: Exception) {
            // Provide user-friendly errors for registration failures
            val errorMessage = when (e.message) {
                "The email address is already in use by another account." -> "Ya existe una cuenta con este correo."
                "The password must be 6 characters long or more." -> "La contraseña debe tener al menos 6 caracteres."
                else -> e.message ?: "Error desconocido durante el registro."
            }
            throw Exception(errorMessage)
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            // 1. Sign in user using Firebase Auth
            val authResult = firebaseService.auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al iniciar sesión.")

            // 2. On successful login, return the secure UID as the session token
            return LoginResponse(token = firebaseUser.uid)
        } catch (e: Exception) {
            // Provide user-friendly errors for login failures
            val errorMessage = when {
                e.message?.contains("There is no user record corresponding to this identifier") == true ||
                        e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true -> "Credenciales incorrectas. Verifica tu email y contraseña."
                else -> e.message ?: "Fallo al iniciar sesión."
            }
            throw Exception(errorMessage)
        }
    }

    suspend fun forgotPassword(email: String) {
        try {
            // Use Firebase Auth to send password reset email
            firebaseService.auth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            // Provide user-friendly errors for password reset failures
            val errorMessage = when {
                e.message?.contains("There is no user record corresponding to this identifier") == true -> "Usuario no encontrado para el email: $email"
                e.message?.contains("The email address is badly formatted") == true -> "Formato de correo electrónico incorrecto."
                else -> e.message ?: "Error desconocido al enviar el email de restablecimiento."
            }
            throw Exception(errorMessage)
        }
    }
    fun logout() {
        firebaseService.auth.signOut()
    }
}
