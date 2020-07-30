package com.example.grouporganizer.Retrofit;

//interface handles the API's that go to the NodeJS server

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {

    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);


    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);


    @POST("teamRegister")
    @FormUrlEncoded
    Observable<String> teamRegister(@Field("email") String email,
                                    @Field("name") String name);


    @POST("teamJoin")
    @FormUrlEncoded
    Observable<String> teamJoin(@Field("email") String email,
                                    @Field("entryid") String entryid);

    @POST("leaveTeam")
    @FormUrlEncoded
    Observable<String> leaveTeam(@Field("email") String email,
                                    @Field("selectTeam") String selectTeam);

    @POST("getTeams")
    @FormUrlEncoded
    Call<List<Team>> getTeams(@Field("email") String email);


    @POST("getGenThreadLst")
    @FormUrlEncoded
    Call<List<Messages>> getGenThreadLst(@Field("entryid") String entryID);

    @POST("authenticateToken")
    @FormUrlEncoded
    Call<String> authenticateToken(@Field("jwt") String jwt);

    @POST("sendgenmsg")
    @FormUrlEncoded
    Call<List<Messages>> sendgenmsg(@Field("user") String user,
                                    @Field("message") String message,
                                    @Field("teamname") String entryID,
                                    @Field("timestamp") Date date);

    @POST("checkAdmin")
    @FormUrlEncoded
    Observable<String> checkAdmin(@Field("email") String user,
                       @Field("entryID") String entryID);

    @POST("getTasks")
    @FormUrlEncoded
    Call<List<Task>> getTasks(@Field("email") String email,
                              @Field("entryID") String entryID);


}
