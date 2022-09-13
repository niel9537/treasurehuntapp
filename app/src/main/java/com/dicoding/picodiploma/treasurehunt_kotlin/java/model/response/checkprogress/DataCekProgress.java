package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.CurrentFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.CurrentPost;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.NextFlow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataCekProgress {
    @SerializedName("current_post")
    @Expose
    private CurrentPost currentPost;
    @SerializedName("next_post")
    @Expose
    private NextPost nextPost = null;
    @SerializedName("current_flow")
    @Expose
    private CurrentFlow currentFlow;
    @SerializedName("next_flow")
    @Expose
    private NextFlow nextFlow;

    public DataCekProgress(CurrentPost currentPost, NextPost nextPost, CurrentFlow currentFlow, NextFlow nextFlow) {
        this.currentPost = currentPost;
        this.nextPost = nextPost;
        this.currentFlow = currentFlow;
        this.nextFlow = nextFlow;
    }

    public CurrentPost getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(CurrentPost currentPost) {
        this.currentPost = currentPost;
    }

    public NextPost getNextPost() {
        return nextPost;
    }

    public void setNextPost(NextPost nextPost) {
        this.nextPost = nextPost;
    }

    public CurrentFlow getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(CurrentFlow currentFlow) {
        this.currentFlow = currentFlow;
    }

    public NextFlow getNextFlow() {
        return nextFlow;
    }

    public void setNextFlow(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }
}
