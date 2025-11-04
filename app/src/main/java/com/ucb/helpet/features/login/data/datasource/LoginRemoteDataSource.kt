package com.ucb.helpet.features.login.data.datasource

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

// 1. Data Source Class
class LoginRemoteDataSource(private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String, userType: String) {
        val request = RegisterRequest(name = name, email = email, password = password, role = userType)
        apiService.register(request)
    }
}

// 2. Retrofit API Interface
interface ApiService {
    @POST("api/auth/register") // Asumiendo que esta es la ruta de tu API
    suspend fun register(@Body body: RegisterRequest)
}

// 3. Data Transfer Object (DTO) for the request
data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String
)