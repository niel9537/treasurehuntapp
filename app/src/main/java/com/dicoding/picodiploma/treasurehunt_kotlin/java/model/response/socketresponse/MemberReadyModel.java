package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse;

import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberReadyModel {
    @SerializedName("member")
    @Expose
    private DataMemberReadyModel member;

    public MemberReadyModel(DataMemberReadyModel member) {
        this.member = member;
    }

    public DataMemberReadyModel getMember() {
        return member;
    }

    public void setMember(DataMemberReadyModel member) {
        this.member = member;
    }
}
