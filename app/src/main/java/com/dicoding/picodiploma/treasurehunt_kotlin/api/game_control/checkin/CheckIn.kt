package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.checkin

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow.NextFlowData

data class CheckIn(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : NextFlowData?
)
