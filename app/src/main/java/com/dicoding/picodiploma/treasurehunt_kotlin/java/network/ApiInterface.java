package com.dicoding.picodiploma.treasurehunt_kotlin.java.network;


import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckIn;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckOut;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.InputGameCodeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LobbyDetailModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ReadyModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.UserMeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress.CekProgressModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //Login
    @POST("mobile/v1/auth")
    Call<LoginModel> login(@Body RequestLogin requestLogin);
    @GET("/mobile/v1/me")
    Call<UserMeModel> profilme(@Header("Authorization") String token);
    @POST("mobile/v1/registration")
    Call<LoginModel> registration(@Body RequestRegister requestRegister);
    @POST("mobile/v1/game-controls/join-game")
    Call<InputGameCodeModel> joinGame(@Header("Authorization") String token, @Body RequestJoinGame requestJoinGame);
    @GET("/mobile/v1/game-controls/me")
    Call<MeModel> me(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/ready-check")
    Call<ReadyModel> ready(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/not-ready-check")
    Call<ReadyModel> unready(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/start-game")
    Call<PlayModel> play(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/next-flow")
    Call<PlayModel> next(@Header("Authorization") String token,  @Query("game_token") String userGameToken,@Body RequestNextFlow requestNextFlow);
    @POST("/mobile/v1/game-controls/check-in")
    Call<PlayModel> cekin(@Header("Authorization") String token,  @Query("game_token") String userGameToken,@Body RequestCheckIn requestCheckIn);
    @POST("/mobile/v1/game-controls/check-out")
    Call<PlayModel> cekout(@Header("Authorization") String token,  @Query("game_token") String userGameToken,@Body RequestCheckOut requestCheckOut);
    @GET("/mobile/v1/game-controls/check-progress")
    Call<CekProgressModel> cekproses(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @GET("/mobile/v1/game-controls/lobbies/{id}")
    Call<LobbyDetailModel> lobbydetail(@Header("Authorization") String token,@Path("id") String lobbyId, @Query("game_token") String userGameToken );
}
