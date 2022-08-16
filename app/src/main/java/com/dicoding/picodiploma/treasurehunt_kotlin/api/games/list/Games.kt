package com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list

data class Games(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : ArrayList<GameDatas>
)
