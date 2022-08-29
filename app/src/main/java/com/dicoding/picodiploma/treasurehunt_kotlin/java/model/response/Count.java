package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Count {
    @SerializedName("game_posts")
    @Expose
    private Integer gamePosts;

    public Count(Integer gamePosts) {
        this.gamePosts = gamePosts;
    }

    public Integer getGamePosts() {
        return gamePosts;
    }

    public void setGamePosts(Integer gamePosts) {
        this.gamePosts = gamePosts;
    }
}
