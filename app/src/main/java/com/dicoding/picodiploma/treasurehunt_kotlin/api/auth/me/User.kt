package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.me


data class User(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : UserData
)
