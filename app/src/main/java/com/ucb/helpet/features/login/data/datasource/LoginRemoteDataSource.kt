package com.ucb.helpet.features.login.data.datasource

import com.google.firebase.database.FirebaseDatabase
import com.ucb.helpet.features.login.data.api.FirebaseService
import com.ucb.helpet.features.login.data.mapper.UserMapper
import com.ucb.helpet.features.login.data.model.LoginResponse
import com.ucb.helpet.features.login.data.model.UserDto
import com.ucb.helpet.features.login.domain.model.Email
import com.ucb.helpet.features.login.domain.model.Location
import com.ucb.helpet.features.login.domain.model.Phone
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.presentation.register.UserType
import kotlinx.coroutines.tasks.await

class LoginRemoteDataSource(private val firebaseService: FirebaseService) {

    private val usersRef = FirebaseDatabase.getInstance().getReference("users")

    suspend fun register(name: String, email: String, password: String, userType: UserType, phone: String, location: String) {
        try {
            val authResult = firebaseService.auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al crear el usuario.")

            val domainUser = User(
                userId = firebaseUser.uid,
                name = name,
                email = Email.create(email),
                phone = if(phone.isNotBlank()) Phone.create(phone) else null,
                location = if(location.isNotBlank()) Location.create(location) else null,
                userType = userType
            )

            val userDto = UserMapper.toDto(domainUser)

            usersRef.child(firebaseUser.uid).setValue(userDto).await()

        } catch (e: Exception) {
            throw Exception(e.message ?: "Error desconocido durante el registro.")
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val authResult = firebaseService.auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user ?: throw Exception("Error al iniciar sesión.")
        return LoginResponse(token = firebaseUser.uid)
    }

    suspend fun forgotPassword(email: String) {
        firebaseService.auth.sendPasswordResetEmail(email).await()
    }

    fun logout() {
        firebaseService.auth.signOut()
    }

    suspend fun getUser(userId: String): User {
        val snapshot = usersRef.child(userId).get().await()
        val userDto = snapshot.getValue(UserDto::class.java) ?: throw Exception("Usuario no encontrado")

        return UserMapper.toDomain(userDto)
    }

    fun getCurrentUserId(): String? {
        return firebaseService.auth.currentUser?.uid
    }
}
