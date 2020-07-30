package com.example.grouporganizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.Messages;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskFragment extends Fragment {
    List<Task> db;
    RecyclerView rv;
    TaskListAdapter adapter;
    Retrofit retrofit = RetrofitClient.getInstance();
    IMyService iMyService = retrofit.create(IMyService.class);
    String teamID;


    private static final String[] paths = {"Sort By: ", "Deadline", "Title", "Date Assigned"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_task, container, false);

        db = new ArrayList<>();
        rv = v.findViewById(R.id.tskList);
        adapter = new TaskListAdapter(db);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        ImageButton toSearch = (ImageButton) v.findViewById(R.id.task_to_search_btn);
        //TODO:works but we need to convert navigation bar selection to search tab
        toSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlankFragment searchFrag = BlankFragment.newInstance("Search");
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.team_fragment_container, searchFrag);
                fragmentTransaction2.commit();
            }
        });

        //handles sorter
        Spinner spinner = (Spinner) v.findViewById(R.id.task_sort);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Toast.makeText(getContext(), "for deadline", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "for Title", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getContext(), "for date assigned", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ends spinner sorter

        refreshTasks();
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

    void refreshTasks() {
        //attempt to load cached email into string data - retrieved from successful login
        Call<List<Task>> tasksCall = iMyService.getTasks(loadEmail(),loadEntryId());
        tasksCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.body() != null) {
                    db.clear();
                    db.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
