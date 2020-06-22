package com.example.grouporganizer.Retrofit;

import com.google.gson.annotations.SerializedName;
public class Team {
    private String teamname;
    private String entryID;
    private String teamObjectID;

    public String getName() {
        return teamname;
    }
    public String getEntryID() {
        return entryID;
    }
    public String getTeamID(){
        return teamObjectID;
    }
}
