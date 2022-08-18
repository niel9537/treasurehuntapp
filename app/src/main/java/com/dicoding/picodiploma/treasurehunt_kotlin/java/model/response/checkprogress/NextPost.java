package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NextPost {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("is_last")
    @Expose
    private Boolean isLast;
    @SerializedName("post_order")
    @Expose
    private Integer postOrder;
    @SerializedName("flows")
    @Expose
    private List<Flow> flows = null;

    public NextPost(String id, String gameId, String title, String postType, Boolean isLast, Integer postOrder, List<Flow> flows) {
        this.id = id;
        this.gameId = gameId;
        this.title = title;
        this.postType = postType;
        this.isLast = isLast;
        this.postOrder = postOrder;
        this.flows = flows;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    public Integer getPostOrder() {
        return postOrder;
    }

    public void setPostOrder(Integer postOrder) {
        this.postOrder = postOrder;
    }

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }
}
