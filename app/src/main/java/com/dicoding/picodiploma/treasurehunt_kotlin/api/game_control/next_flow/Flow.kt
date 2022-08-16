package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.file.File

data class Flow(
    val id : String,
    val content : String,
    val flow_order : String,
    val flow_type : FlowType?,
    val is_last : Boolean,
    val post_id : String,
    val file : File?
)
