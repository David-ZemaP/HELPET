package com.ucb.helpet.features.login.data.datasource

import com.ucb.helpet.features.login.data.api.FirebaseService
import com.ucb.helpet.features.login.data.model.LoginResponse
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.presentation.register.UserType
import kotlinx.coroutines.tasks.await

class LoginRemoteDataSource(private val firebaseService: FirebaseService) {

    private val usersRef = firebaseService.database.getReference("users")

    suspend fun register(name: String, email: String, password: String, userType: UserType) {
        // Sanitize email to use it as a key in Firebase, as keys cannot contain '.'
        val userKey = email.replace(".", "_dot_")

        // Check if a user with this email already exists
        val existingUserSnapshot = usersRef.child(userKey).get().await()
        if (existingUserSnapshot.exists()) {
            throw Exception("Un usuario con este email ya existe.")
        }

        // Create a new user object and save it to Firebase
        val user = User(
            userId = userKey,
            name = name,
            email = email,
            password = password, // WARNING: Storing plain text passwords is not secure for production.
            userType = userType
        )
        usersRef.child(userKey).setValue(user).await()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val userKey = email.replace(".", "_dot_")

        val userSnapshot = usersRef.child(userKey).get().await()

        if (!userSnapshot.exists()) {
            throw Exception("Usuario no encontrado.")
        }

        val user = userSnapshot.getValue(User::class.java)
            ?: throw Exception("Error al procesar los datos del usuario.")

        if (user.password != password) {
            throw Exception("Contraseña incorrecta.")
        }

        // On successful login, return a LoginResponse.
        // For simplicity, we'll use the user's ID (the sanitized email) as the session token.
        return LoginResponse(token = user.userId)
    }

    suspend fun forgotPassword(email: String) {
        // This functionality typically requires Firebase Auth. 
        // For Realtime Database only, this is not straightforward.
        throw NotImplementedError("La funcionalidad de recuperar contraseña aún no está implementada con Firebase.")
    }
}
