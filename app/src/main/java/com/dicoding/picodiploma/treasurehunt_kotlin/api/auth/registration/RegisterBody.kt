package com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("full_name") val full_name : String,
    @SerializedName("address") val address : String,
    @SerializedName("phone_number") val phone_number : String
)
