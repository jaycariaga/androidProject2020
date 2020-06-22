package com.example.grouporganizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentTeamsList extends Fragment {
    List<Team> db;
    RecyclerView rv;
    TeamsListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_teams_list, container, false);
        db = new ArrayList<>();
        adapter = new TeamsListAdapter(db);
        rv = v.findViewById(R.id.teams_list_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        Retrofit retrofit = RetrofitClient.getInstance();
        IMyService iMyService = retrofit.create(IMyService.class);

        adapter.setOnItemClickListener(new TeamsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                 db.get(position);
                Toast.makeText(getContext(), "selected a team", Toast.LENGTH_SHORT).show();
            }
        });

        //attempt to load cached email into string data - retrieved from successful login
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String emaildata = sharedPreferences.getString("email", "") ;

        Call<List<Team>> teamCall = iMyService.getTeams(emaildata);
        teamCall.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if (response.body() != null) {
                    db.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Snackbar.make(v, "Found " + response.body().size() + " teams", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                Snackbar.make(v, t.getMessage(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
            }
        });
        return v;
    }

    void updateViews() {

    }
}
