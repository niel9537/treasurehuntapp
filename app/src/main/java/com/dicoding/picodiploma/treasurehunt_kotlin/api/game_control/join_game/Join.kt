package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game


data class Join(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : JoinData?
)
