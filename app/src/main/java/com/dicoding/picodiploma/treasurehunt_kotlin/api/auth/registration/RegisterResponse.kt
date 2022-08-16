package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration

import androidx.annotation.Nullable

data class RegisterResponse(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val errors : Nullable,
    val data : RegisterResponseData
)
