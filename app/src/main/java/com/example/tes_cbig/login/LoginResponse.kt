package com.example.tes_cbig.login

data class LoginResponse(
    val statusCode: Int,
    val message: String,
    val data: UserData
)

data class UserData(
    val email: String,
    val name: String,
    val department: String,
    val position: String,
    val api_token: String
)