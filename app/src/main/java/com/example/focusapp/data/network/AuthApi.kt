package com.example.focusapp.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiEnvelope<UserDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiEnvelope<AuthPayload>

    @GET("auth/me")
    suspend fun getMe(): ApiEnvelope<UserDto>
}

