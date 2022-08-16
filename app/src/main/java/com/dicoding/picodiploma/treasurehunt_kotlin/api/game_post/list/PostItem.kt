package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.list

data class PostItem(
    val id : String,
    val title : String,
    val post_type : String,
    val access_key : String,
    val post_order : Int,
    val is_last : Boolean,
    val _count : Count?
)
