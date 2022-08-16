package com.dicoding.picodiploma.treasurehunt_kotlin.data

import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class RegisterUserData(
    @Nullable
    @SerializedName("email")
    var emailRegister : String?,

    @SerializedName("password")
    var passwordRegister : String?,

    @SerializedName("full_name")
    var fullNameRegister : String?,

    @SerializedName("address")
    var addressRegister : String?,

    @SerializedName("phone_number")
    var phoneRegister : String?
)
