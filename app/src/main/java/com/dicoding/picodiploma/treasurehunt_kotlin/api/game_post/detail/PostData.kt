package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.detail

data class PostData(
    val id : String,
    val access_key : String,
    val post_type : String,
    val post_order : Int,
    val title : String,
    val game_id : String,
    val is_last : Boolean,
    val created_at : String,
    val updated_at : String,
    val _count : Count
)
