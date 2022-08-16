package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.start_game

data class StartGame (
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : StartGameData?
        )