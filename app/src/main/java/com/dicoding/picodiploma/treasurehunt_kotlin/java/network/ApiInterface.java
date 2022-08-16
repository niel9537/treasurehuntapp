package com.dicoding.picodiploma.treasurehunt_kotlin.java.network;


import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.requestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.requestRegister;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    //Login
    @POST("mobile/v1/auth")
    Call<LoginModel> login(@Body requestLogin requestLogin);
    @POST("mobile/v1/registration")
    Call<LoginModel> registration(@Body requestRegister requestRegister);
}
