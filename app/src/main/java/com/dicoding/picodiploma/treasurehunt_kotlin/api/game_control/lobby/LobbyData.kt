package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.lobby

data class LobbyData(
    val id : String,
    val name : String,
    val game_id : String,
    val status : String,
    val party_members : List<Member>
)