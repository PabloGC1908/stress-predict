package com.pgc.stresspredict.data.model

data class AuthResponse(
    val username: String,
    val message: String,
    val jwt: String,
    val status: Boolean
)