package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.detail

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.list.PostItem

data class Detail(
    val isSuccess : Boolean,
    val responseCode : Int,
    val responseMessage : String,
    val message : String,
    val data : PostData?
)
