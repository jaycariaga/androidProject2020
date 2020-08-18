package com.example.grouporganizer.Retrofit;

//interface handles the API's that go to the NodeJS server

import java.util.ArrayList;
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

//used in: SignUp
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password);

//used in: MainActivity
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);


//used in: FragmentRegisterTeam
    @POST("teamRegister")
    @FormUrlEncoded
    Observable<String> teamRegister(@Field("email") String email,
                                    @Field("name") String name);

//used in: FragmentTeamsList
    @POST("teamJoin")
    @FormUrlEncoded
    Observable<String> teamJoin(@Field("email") String email,
                                    @Field("entryid") String entryid);

//TODO: used within: TeamActivity
    @POST("leaveTeam")
    @FormUrlEncoded
    Observable<String> leaveTeam(@Field("email") String email,
                                    @Field("selectTeam") String selectTeam);


//FragmentTeamsList
    @POST("getTeams")
    @FormUrlEncoded
    Call<List<Team>> getTeams(@Field("email") String email);


//used in: MessageFragment
    @POST("getGenThreadLst")
    @FormUrlEncoded
    Call<List<Messages>> getGenThreadLst(@Field("entryid") String entryID);


//used in: MainActivity
    @POST("authenticateToken")
    @FormUrlEncoded
    Call<String> authenticateToken(@Field("jwt") String jwt);


//used in: MessageFragment
    @POST("sendgenmsg")
    @FormUrlEncoded
    Call<List<Messages>> sendgenmsg(@Field("user") String user,
                                    @Field("message") String message,
                                    @Field("teamname") String entryID,
                                    @Field("timestamp") Date date);

//used in: TeamsListAdapter
    @POST("checkAdmin")
    @FormUrlEncoded
    Call<Integer> checkAdmin(@Field("email") String user,
                       @Field("entryID") String entryID);


//used in: TaskFragment
    @POST("getTasks")
    @FormUrlEncoded
    Call<List<Task>> getTasks(@Field("email") String email,
                              @Field("entryID") String entryID);


//used in: TaskFragment
    @POST("getTeamMembers")
    @FormUrlEncoded
    Call<ArrayList<String>> getTeamMembers(@Field("entryID") String entryID);


//used in: Task Fragment
    @POST("postTask")
    @FormUrlEncoded
    Observable<String> postTask(@Field("email") String email,
                                @Field("entryID") String entryID,
                                @Field("descript") String descript,
                                @Field("title") String title,
                                @Field("deadline") String deadline,
                                @Field("assigneduser") String assignee,
                                @Field("tags") String tags );


//used in:
    @POST("searchgetGen")
    @FormUrlEncoded
    Call<ArrayList<SearchInTeam>> searchgetGen(
            @Field("entryID") String entryID
    );



}
