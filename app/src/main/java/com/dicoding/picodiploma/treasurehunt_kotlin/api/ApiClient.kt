package com.dicoding.picodiploma.treasurehunt_kotlin.api

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.ready_check.ReadyCheck
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail.Game
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list.Games
import com.dicoding.picodiploma.treasurehunt_kotlin.data.RegisterUserData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    @POST("/mobile/v1/registration")
    fun registerUser(
        @Path("email") email : String,
        @Path("password") password : String,
        @Path("full_name") fullName : String,
        @Path("address") address : String,
        @Path("phone_number") phoneNumber : String
    ) : Call<RegisterUserData>

    @GET("/mobile/v1/games")
    fun getGameLists(@Header("Authorization") token: String) : Call<Games>

    @GET("/mobile/v1/games/{gameID}")
    suspend fun getGameDetail(@Header("Authorization") token: String,
                              @Path("gameID") gameID: String) : Response<Game>

    @POST("/mobile/v1/game-controls/ready-check")
    fun readyCheck(@Header("Authorization") token: String,
                   @Query("game_token") game_token: String
    ) : Call<ReadyCheck>

}