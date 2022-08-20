package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.NextFlow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameStartedModel {
    @SerializedName("next_flow")
    @Expose
    private NextFlow nextFlow;

    public GameStartedModel(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }

    public NextFlow getNextFlow() {
        return nextFlow;
    }

    public void setNextFlow(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }
}
