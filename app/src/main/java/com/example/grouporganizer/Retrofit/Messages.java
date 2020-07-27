package com.example.grouporganizer.Retrofit;

public class Messages {
    private String message;
    private String timestamp;
    private String user;

    public String getGenMsg(){
        return message;
    }
    public String getTimestamp(){
        String[] outTime = timestamp.split(" ");
        return outTime[1] + " " + outTime[2] + " " + outTime[3] + " " + outTime[5];
    }
    public String getSender(){return user;}



}
