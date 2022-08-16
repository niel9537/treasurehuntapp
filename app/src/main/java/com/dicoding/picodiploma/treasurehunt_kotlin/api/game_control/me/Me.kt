package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.me

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.JoinData

data class Me(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : MeData?
)
