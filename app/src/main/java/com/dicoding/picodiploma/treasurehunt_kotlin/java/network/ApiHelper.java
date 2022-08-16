package com.dicoding.picodiploma.treasurehunt_kotlin.java.network;

import android.util.Base64;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;

import java.io.UnsupportedEncodingException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(Config.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }
        return retrofit;
    }
}
