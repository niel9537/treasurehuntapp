package com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail


data class Game(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : GameData?
)
