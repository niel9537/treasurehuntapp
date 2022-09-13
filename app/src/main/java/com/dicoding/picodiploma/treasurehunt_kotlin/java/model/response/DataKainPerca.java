package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKainPerca {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_classification")
    @Expose
    private String gameClassification;
    @SerializedName("group")
    @Expose
    private Integer group;
    @SerializedName("file")
    @Expose
    private File file;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public DataKainPerca(String id, String gameClassification, Integer group, File file, String name, String updatedAt, String createdAt) {
        this.id = id;
        this.gameClassification = gameClassification;
        this.group = group;
        this.file = file;
        this.name = name;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameClassification() {
        return gameClassification;
    }

    public void setGameClassification(String gameClassification) {
        this.gameClassification = gameClassification;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
