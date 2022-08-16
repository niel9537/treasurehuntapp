package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request;

public class RequestLogin {
    final String email;
    final String password;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
