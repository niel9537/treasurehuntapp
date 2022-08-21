package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request;

public class RequestCheckOut {
    final String post_id;
    final String flow_id;

    public RequestCheckOut(String post_id, String flow_id) {
        this.post_id = post_id;
        this.flow_id = flow_id;
    }
}
