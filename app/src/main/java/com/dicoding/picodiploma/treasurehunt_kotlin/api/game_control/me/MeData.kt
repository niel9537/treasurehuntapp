package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.me

data class MeData(
    val id : String,
    val badge : String,
    val status : String,
    val party_id : String,
    val user : MeDataUser?
)
