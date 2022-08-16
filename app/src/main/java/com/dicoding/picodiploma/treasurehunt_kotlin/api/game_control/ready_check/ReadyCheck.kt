package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.ready_check

class ReadyCheck (
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : ReadyCheckData?
    )