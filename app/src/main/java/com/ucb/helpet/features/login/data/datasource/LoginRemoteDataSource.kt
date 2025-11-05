package com.ucb.helpet.features.login.data.datasource

class LoginRemoteDataSource(private val apiService: ApiService) {

    suspend fun register(name: String, email: String, password: String, userType: String) {
        val response = apiService.register(mapOf("name" to name, "email" to email, "password" to password, "userType" to userType))
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
    }

    suspend fun forgotPassword(email: String) {
        val response = apiService.forgotPassword(mapOf("email" to email))
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string())
        }
    }
}
