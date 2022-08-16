package com.dicoding.picodiploma.treasurehunt_kotlin.api.games

import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail.Game
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list.Games
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GameInterface {
    @GET("/mobile/v1/games")
    suspend fun getGameLists(@Header("Authorization") token: String) : Response<Games>

    @GET("/mobile/v1/games/{gameID}")
    suspend fun getGameDetail(@Header("Authorization") token: String,
                              @Path("gameID") gameID: String) : Response<Game>
}