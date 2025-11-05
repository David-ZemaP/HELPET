package com.ucb.helpet.features.login.data.datasource

import com.ucb.helpet.features.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<Unit>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: Map<String, String>): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>
}
