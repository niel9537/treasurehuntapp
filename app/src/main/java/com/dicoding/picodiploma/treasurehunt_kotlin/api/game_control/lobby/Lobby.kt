package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.lobby


data class Lobby(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : LobbyData?
)
