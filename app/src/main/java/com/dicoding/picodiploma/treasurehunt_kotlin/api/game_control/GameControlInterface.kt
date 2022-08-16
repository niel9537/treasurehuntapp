package com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control

import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.check_progress.CheckProgress
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.Join
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.JoinBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.lobby.Lobby
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.me.Me
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow.Flow
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.next_flow.NextFlow
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.ready_check.ReadyCheck
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.start_game.StartGame
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GameControlInterface {
    @Headers("Content-Type: application/json")
    @POST("/mobile/v1/game-controls/join-game")
    fun join(@Header("Authorization") token: String, @Body code : JoinBody) : Call<Join>

    @GET("/mobile/v1/game-controls/me")
    fun getUserInfoInGame(@Header("Authorization") token: String,
                               @Query("game_token") game_token: String) : Call<Me>

    @GET("/mobile/v1/game-controls/lobbies/{lobbyID}")
    suspend fun getLobbyDetail(@Header("Authorization") token: String,
                               @Path("lobbyID") lobbyID : String,
                               @Query("game_token") game_token: String,) : Response<Lobby>


    @POST("/mobile/v1/game-controls/ready-check")
    fun readyCheck(@Header("Authorization") token: String,
                           @Query("game_token") game_token: String
    ) : Call<ReadyCheck>

    @POST("/mobile/v1/game-controls/start-game")
    fun startGame(@Header("Authorization") token: String,
                   @Query("game_token") game_token: String) : Call<StartGame>

    @POST("/mobile/v1/game-controls/next-flow")
    fun nextFlow(@Header("Authorization") token: String,
                 @Query("game_token") game_token: String,
                 @Body flow_id : JoinBody) : Response<NextFlow>

    @POST("/mobile/v1/game-controls/next-flow")
    fun checkProgress(@Header("Authorization") token: String,
                 @Query("game_token") game_token: String) : Response<CheckProgress>
/*
    @POST("/mobile/v1/game-controls/start-game")
    fun startGame(@Header("Authorization") token: String,
                  @Query("game_token") game_token: String
    ) : Response<Me>

 */

}