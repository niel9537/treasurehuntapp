package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class requestRegister {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    public requestRegister(String email, String password, String fullName, String address, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
