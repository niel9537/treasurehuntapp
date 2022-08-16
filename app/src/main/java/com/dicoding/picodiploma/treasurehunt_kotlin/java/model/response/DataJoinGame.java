package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataJoinGame {
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("lobby_id")
    @Expose
    private String lobbyId;
    @SerializedName("game_token")
    @Expose
    private String gameToken;

    public DataJoinGame(String gameId, String lobbyId, String gameToken) {
        this.gameId = gameId;
        this.lobbyId = lobbyId;
        this.gameToken = gameToken;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }
}
