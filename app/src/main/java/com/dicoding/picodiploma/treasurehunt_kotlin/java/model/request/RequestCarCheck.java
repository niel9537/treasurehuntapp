package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request;

public class RequestCarCheck {
    final String transport_id;
    final String flow_id;

    public RequestCarCheck(String transport_id, String flow_id) {
        this.transport_id = transport_id;
        this.flow_id = flow_id;
    }

    public String getTransport_id() {
        return transport_id;
    }

    public String getFlow_id() {
        return flow_id;
    }
}
