package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataReadyModel {
    @SerializedName("member_id")
    @Expose
    private String member_id;

    public DataReadyModel(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
