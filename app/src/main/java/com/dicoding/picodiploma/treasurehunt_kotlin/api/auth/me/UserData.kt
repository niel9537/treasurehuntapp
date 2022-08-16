package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.me

data class UserData(
    val id : String,
    val email : String,
    val role : String,
    val status : String,
    val profile : UserProfile

)
