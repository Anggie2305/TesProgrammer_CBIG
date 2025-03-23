package com.example.tes_cbig.api

import com.example.tes_cbig.login.LoginResponse
import com.example.tes_cbig.main.ItemResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("api/dev/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("api/dev/list-items")
    fun getItems(
        @Header("Authorization") token: String // Gunakan token sebagai header Authorization
    ): Call<ItemResponse>
}