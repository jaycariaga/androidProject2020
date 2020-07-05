package com.example.grouporganizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentTeamsList extends Fragment {
    List<Team> db;
    RecyclerView rv;
    TeamsListAdapter adapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Retrofit retrofit = RetrofitClient.getInstance();
    IMyService iMyService = retrofit.create(IMyService.class);

    @Override
    public void onStop(){
        compositeDisposable.clear();
        super.onStop();
    } //onStop needed by CompositeDisposable to destroy unneeded objects

    OnTeamSelectedListener callback;
    public void setOnTeamSelectedListener(OnTeamSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnTeamSelectedListener {
        public void onTeamSelected(String team);
    }

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

        //carries joinTeamAPI upon button click
        final Button TeamPagetrans = v.findViewById(R.id.joinNewTeam);
        final EditText entryID = v.findViewById(R.id.joinENTRYid);
        TeamPagetrans.setOnClickListener(new View.OnClickListener(){
            @Override
          public void onClick(View v){
                if(TextUtils.isEmpty(entryID.getText().toString())){
                    Toast.makeText(getContext(),"Must Fill in Field", Toast.LENGTH_SHORT).show();
                    return;
                }
                compositeDisposable.add(iMyService.teamJoin(loadEmail(), entryID.getText().toString()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                Reload();
                            }
                        }));
                //System.out.println("Hello World");
            }
        });

        //item click transitions to team page
        adapter.setOnItemClickListener(new TeamsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Team selectedTm = db.get(position);
                saveTeamPref(selectedTm.getName(), selectedTm.getEntryID());
                callback.onTeamSelected(selectedTm.getTeamID());

            }
        });

        //attempt to load cached email into string data - retrieved from successful login
        Call<List<Team>> teamCall = iMyService.getTeams(loadEmail());
        teamCall.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if (response.body() != null) {
                    db.addAll(response.body());
                    adapter.notifyDataSetChanged();
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

    public void Reload(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(FragmentTeamsList.this.getId(), new FragmentTeamsList()).commit();
    }

    private void saveTeamPref(String team, String entryID){
        //local storage of currently logged in user...
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("teamname", team); //stores the TEAM NAME of the stored team
        editor.putString("teamID", entryID); //teamID stores the ENTRYID of the team being joined
        editor.commit();
        System.out.println(team);
    }

    private String loadEmail(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String data = sharedPreferences.getString("email", "");
        return data;
    }


}
