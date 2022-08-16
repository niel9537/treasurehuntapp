package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.ready_check

data class ReadyCheckData (
    val id : String,
    val party_id : String,
    val user_id : String,
    val status : String,
    val online_status : String,
    val badge : String,
     val created_at : String,
    val updated_at : String
    )