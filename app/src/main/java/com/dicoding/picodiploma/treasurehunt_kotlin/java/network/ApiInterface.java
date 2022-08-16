package com.dicoding.picodiploma.treasurehunt_kotlin.java.network;


import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.InputGameCodeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ReadyModel;

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
    @POST("mobile/v1/registration")
    Call<LoginModel> registration(@Body RequestRegister requestRegister);
    @POST("mobile/v1/game-controls/join-game")
    Call<InputGameCodeModel> joinGame(@Header("Authorization") String token, @Body RequestJoinGame requestJoinGame);
    @GET("/mobile/v1/game-controls/me")
    Call<MeModel> me(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/ready-check")
    Call<ReadyModel> ready(@Header("Authorization") String token, @Query("game_token") String userGameToken);
    @POST("/mobile/v1/game-controls/start-game")
    Call<ReadyModel> play(@Header("Authorization") String token, @Query("game_token") String userGameToken);
}
