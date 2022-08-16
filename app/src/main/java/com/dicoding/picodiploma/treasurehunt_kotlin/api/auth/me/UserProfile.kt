package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.me

import androidx.annotation.Nullable

data class UserProfile(
    val id : String,
    val full_name : String,
    val address : String,
    val phone_number : String,
    val avatar : Nullable,
)
