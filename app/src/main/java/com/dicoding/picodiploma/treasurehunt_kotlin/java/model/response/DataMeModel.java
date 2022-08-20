package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMeModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("badge")
    @Expose
    private String badge;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;
    @SerializedName("party_id")
    @Expose
    private String partyId;
    @SerializedName("user")
    @Expose
    private User user;

    public DataMeModel(String id, String badge, String status, String onlineStatus, String partyId, User user) {
        this.id = id;
        this.badge = badge;
        this.status = status;
        this.onlineStatus = onlineStatus;
        this.partyId = partyId;
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

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
