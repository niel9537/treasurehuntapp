package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth

import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login.LoginBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login.LoginResponse
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration.RegisterBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.me.User
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthInterface {
    @Headers("Content-Type: application/json")
    @POST("/mobile/v1/registration")
    suspend fun registUser(@Body user : RegisterBody) : Response<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("/mobile/v1/auth")
    fun login(@Body user : LoginBody) : Call<LoginResponse>


    @GET("/mobile/v1/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String) : Response<User>

//    @POST("mobile/v1/registration")
//    suspend fun register(@Body user: List<RegisterBody>): Response<User>



}