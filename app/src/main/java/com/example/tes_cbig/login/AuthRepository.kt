package com.example.tes_cbig.login

import com.example.tes_cbig.api.ApiClient
import retrofit2.await

class AuthRepository {
    private val apiService = ApiClient.instance

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password).await()
    }
}