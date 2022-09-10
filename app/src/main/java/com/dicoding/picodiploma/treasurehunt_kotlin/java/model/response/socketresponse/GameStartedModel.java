package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.CurrentFlow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameStartedModel {
    @SerializedName("current_flow")
    @Expose
    private CurrentFlow currentFlow;

    public GameStartedModel(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }

    public CurrentFlow getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }
}
