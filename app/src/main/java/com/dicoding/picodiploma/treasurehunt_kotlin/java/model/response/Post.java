package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("post_order")
    @Expose
    private int postOrder;
    @SerializedName("is_last")
    @Expose
    private Boolean isLast;

    public Post(String id, int postOrder, Boolean isLast) {
        this.id = id;
        this.postOrder = postOrder;
        this.isLast = isLast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPostOrder() {
        return postOrder;
    }

    public void setPostOrder(int postOrder) {
        this.postOrder = postOrder;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }
}
