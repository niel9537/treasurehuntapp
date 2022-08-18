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
    private DataCekProgress data;

    public CekProgressModel(Boolean isSuccess, Integer responseCode, String responseMessage, String message, DataCekProgress data) {
        this.isSuccess = isSuccess;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataCekProgress getData() {
        return data;
    }

    public void setData(DataCekProgress data) {
        this.data = data;
    }
}
