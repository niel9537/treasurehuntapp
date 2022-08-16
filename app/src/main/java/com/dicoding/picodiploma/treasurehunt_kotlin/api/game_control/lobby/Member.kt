package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.lobby

data class Member(
    val id : String,
    val status : String,
    val badge : String,
    val user :  MemberProfile?
)
