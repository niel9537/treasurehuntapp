package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentFlow {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
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
    @SerializedName("next")
    @Expose
    private Boolean next;
    @SerializedName("prev")
    @Expose
    private Boolean prev;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("file")
    @Expose
    private File file;

    public CurrentFlow(String id, String title, String subTitle, String content, Integer flowOrder, FlowType flowType, Boolean isLast, Boolean next, Boolean prev, String postId, File file) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.flowOrder = flowOrder;
        this.flowType = flowType;
        this.isLast = isLast;
        this.next = next;
        this.prev = prev;
        this.postId = postId;
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public Boolean getPrev() {
        return prev;
    }

    public void setPrev(Boolean prev) {
        this.prev = prev;
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
