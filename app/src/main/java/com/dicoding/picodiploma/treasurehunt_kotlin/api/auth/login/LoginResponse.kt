package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login

import androidx.annotation.Nullable

data class LoginResponse(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val errors : String?,
    val data : LoginResponseData?
)
