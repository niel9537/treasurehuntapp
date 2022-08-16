package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow

data class Post(
    val id : String,
    val game_id : String,
    val title : String,
    val post_type : String,
    val is_last : Boolean,
    val post_order : String,
    val flows : Flow?
)
