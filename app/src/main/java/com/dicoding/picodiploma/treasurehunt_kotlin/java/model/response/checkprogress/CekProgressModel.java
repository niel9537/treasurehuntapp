package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CekProgressModel {
    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

}
