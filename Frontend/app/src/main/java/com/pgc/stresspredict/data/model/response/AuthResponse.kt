package com.pgc.stresspredict.data.model.response

data class AuthResponse(
    val username: String,
    val message: String,
    val jwt: String,
    val status: Boolean
)