package com.dicoding.picodiploma.treasurehunt_kotlin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBase {
    private const val URL = "https://th-main-api.kartala.id/"

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInterface = retrofit.create(ApiClient::class.java)

}