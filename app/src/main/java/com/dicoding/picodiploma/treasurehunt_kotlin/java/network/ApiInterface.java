package com.dicoding.picodiploma.treasurehunt_kotlin.java.network;


import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.GameModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    //Login
    @POST("mobile/v1/auth")
    Call<LoginModel> login(@Body RequestLogin requestLogin);
    @POST("mobile/v1/registration")
    Call<LoginModel> registration(@Body RequestRegister requestRegister);
    @POST("mobile/v1/game-controls/join-game")
    Call<GameModel> joinGame(@Header("Authorization") String token, @Body RequestJoinGame requestJoinGame);

}
