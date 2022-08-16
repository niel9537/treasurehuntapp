package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextFlow {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("flow_order")
    @Expose
    private Integer flowOrder;
    @SerializedName("flow_type")
    @Expose
    private FlowType flowType;
    @SerializedName("is_last")
    @Expose
    private Boolean isLast;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("file")
    @Expose
    private File file;

    public NextFlow(String id, String content, Integer flowOrder, FlowType flowType, Boolean isLast, String postId, File file) {
        this.id = id;
        this.content = content;
        this.flowOrder = flowOrder;
        this.flowType = flowType;
        this.isLast = isLast;
        this.postId = postId;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFlowOrder() {
        return flowOrder;
    }

    public void setFlowOrder(Integer flowOrder) {
        this.flowOrder = flowOrder;
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType(FlowType flowType) {
        this.flowType = flowType;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
