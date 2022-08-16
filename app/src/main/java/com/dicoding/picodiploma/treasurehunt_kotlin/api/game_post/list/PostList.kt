package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.list


data class PostList(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : List<PostItem>?
)
