package com.example.grouporganizer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.Messages;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {
    List<Messages> msgdb;
    List<Task> tskdb;
    RecyclerView rvTsk;
    RecyclerView rvMsg;
    Retrofit retrofit = RetrofitClient.getInstance();
    IMyService iMyService = retrofit.create(IMyService.class);
    MessageListAdapter msgAdapter;
    TaskListAdapter tskAdapter;
    //SearchAdapter adapter;
    String teamID;
    //ImageButton refresh;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_search, container, false);

        refresh = (ImageButton) v.findViewById(R.id.refreshMessages);


        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(7f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        //editTextMessage.setBackground(sd);


        msgdb = new ArrayList<>();
        tskdb = new ArrayList<>();
        rvMsg = v.findViewById(R.id.message_searching); //must instantiate recyclierview location
        rvTsk = v.findViewById(R.id.task_searching);    //same as above comment
        msgAdapter = new MessageListAdapter(msgdb);
        tskAdapter = new TaskListAdapter(tskdb);

        rvMsg.setAdapter(msgAdapter);
        rvTsk.setAdapter(tskAdapter);
        rvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTsk.setLayoutManager(new LinearLayoutManager(getContext()));

        teamID = getArguments().getString("entryId");

       /* refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMessages();
                Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });*/


        refreshMessages();
        return v;
    }

    private String loadEmail(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String data = sharedPreferences.getString("email", "");
        return data;
    }

    private String loadEntryId(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String data = sharedPreferences.getString("teamID", "");
        return data;
    }

    void refreshMessages() {
        //attempt to load cached email into string data - retrieved from successful login
        Call<SearchInTeam> searchRes = iMyService.searchgetGen(loadEntryId());
        searchRes.enqueue(new Callback<SearchInTeam>() {
            @Override
            public void onResponse(Call<SearchInTeam> call, Response<SearchInTeam> response) {
                if (response.body() != null) {
                    db.clear();
                    db.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchInTeam> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}