package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPlayModel {

    @SerializedName("next_flow")
    @Expose
    private NextFlow nextFlow;

    public DataPlayModel(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }

    public NextFlow getNextFlow() {
        return nextFlow;
    }

    public void setNextFlow(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }
}
