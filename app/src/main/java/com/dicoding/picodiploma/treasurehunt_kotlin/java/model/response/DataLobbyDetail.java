package com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataLobbyDetail {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("party_members")
    @Expose
    private List<PartyMember> partyMembers = null;

    public DataLobbyDetail(String id, String name, String gameId, String status, List<PartyMember> partyMembers) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.status = status;
        this.partyMembers = partyMembers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PartyMember> getPartyMembers() {
        return partyMembers;
    }

    public void setPartyMembers(List<PartyMember> partyMembers) {
        this.partyMembers = partyMembers;
    }
}
