package com.example.focusapp.data.network

data class ApiEnvelope<T>(
    val statusCode: Int,
    val data: T,
    val message: String,
    val success: Boolean
)

