package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMemberReadyModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("badge")
    @Expose
    private String badge;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user")
    @Expose
    private User user;

    public DataMemberReadyModel(String id, String badge, String status, User user) {
        this.id = id;
        this.badge = badge;
        this.status = status;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}