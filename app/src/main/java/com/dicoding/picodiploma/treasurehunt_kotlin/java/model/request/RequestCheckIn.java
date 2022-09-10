package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request;

public class RequestCheckIn {
    final String post_id;
    final String flow_id;

    public RequestCheckIn(String post_id, String flow_id) {
        this.post_id = post_id;
        this.flow_id = flow_id;
    }
}
