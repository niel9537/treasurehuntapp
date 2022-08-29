package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("file_id")
    @Expose
    private String fileId;

    public Banner(String id, String gameId, String fileId) {
        this.id = id;
        this.gameId = gameId;
        this.fileId = fileId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
