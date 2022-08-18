package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.NextFlow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCekProgress {
    @SerializedName("latest_post")
    @Expose
    private LatestPost latestPost;
    @SerializedName("next_post")
    @Expose
    private NextPost nextPost;
    @SerializedName("latest_flow")
    @Expose
    private LatestFlow latestFlow;
    @SerializedName("next_flow")
    @Expose
    private NextFlow nextFlow;

    public DataCekProgress(LatestPost latestPost, NextPost nextPost, LatestFlow latestFlow, NextFlow nextFlow) {
        this.latestPost = latestPost;
        this.nextPost = nextPost;
        this.latestFlow = latestFlow;
        this.nextFlow = nextFlow;
    }

    public LatestPost getLatestPost() {
        return latestPost;
    }

    public void setLatestPost(LatestPost latestPost) {
        this.latestPost = latestPost;
    }

    public NextPost getNextPost() {
        return nextPost;
    }

    public void setNextPost(NextPost nextPost) {
        this.nextPost = nextPost;
    }

    public LatestFlow getLatestFlow() {
        return latestFlow;
    }

    public void setLatestFlow(LatestFlow latestFlow) {
        this.latestFlow = latestFlow;
    }

    public NextFlow getNextFlow() {
        return nextFlow;
    }

    public void setNextFlow(NextFlow nextFlow) {
        this.nextFlow = nextFlow;
    }
}
