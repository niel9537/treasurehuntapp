package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCarCheckModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("current_flow")
    @Expose
    private CurrentFlow currentFlow;
    @SerializedName("message")
    @Expose
    private String message;

    public DataCarCheckModel(String status, CurrentFlow currentFlow, String message) {
        this.status = status;
        this.currentFlow = currentFlow;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CurrentFlow getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
