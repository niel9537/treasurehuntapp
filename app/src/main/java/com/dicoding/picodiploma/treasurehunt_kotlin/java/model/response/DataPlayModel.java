package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPlayModel {

    @SerializedName("current_flow")
    @Expose
    private CurrentFlow currentFlow;

    public DataPlayModel(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }

    public CurrentFlow getNextFlow() {
        return currentFlow;
    }

    public void setNextFlow(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }
}
