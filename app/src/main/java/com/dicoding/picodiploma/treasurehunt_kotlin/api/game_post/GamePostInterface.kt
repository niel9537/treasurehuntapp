package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.detail.Detail
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_post.list.PostList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GamePostInterface {
    @GET("/mobile/v1/game-posts")
    suspend fun getPostList(@Header("Authorization") token: String,
                            @Query("game_id") game_id: String) : Response<PostList>

    @GET("/web/v1/game-posts/{post_id}")
    suspend fun getPostDetail(@Header("Authorization") token: String,
                              @Path("post_id") post_id: String) : Response<Detail>
}