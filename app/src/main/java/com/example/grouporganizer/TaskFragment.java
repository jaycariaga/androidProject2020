package com.example.grouporganizer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.Messages;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
    CompositeDisposable compositeDisposable = new CompositeDisposable();



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

        FloatingActionButton addingTask = (FloatingActionButton) v.findViewById(R.id.addTaskFloater);
        addingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //assigning all builds for task_register layout here
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogview = inflater.inflate(R.layout.task_register, null);
                builder.setTitle("Pick an Action");
                EditText title = (EditText) dialogview.findViewById(R.id.newTskTitle);
                EditText tags = (EditText) dialogview.findViewById(R.id.newTskTag);
                EditText descr = (EditText) dialogview.findViewById(R.id.newTskDscr);
                Spinner users = (Spinner) dialogview.findViewById(R.id.newTskUserAssn); //should be a spinner instead
                TextView tagView = (TextView) dialogview.findViewById(R.id.tag_view_prev);
                Button tagEntBtn = (Button) dialogview.findViewById(R.id.tag_entry); //onclick for tag saving
                Button mveDatePick = (Button) dialogview.findViewById(R.id.move_to_datepick);
                TextView dateDisplay = (TextView) dialogview.findViewById(R.id.project_add_dedlne);

                //tagTotal is the resultant list of tags
                List<String> tagTotal = new ArrayList<>();

                List<String> paths = new ArrayList<String>();
                //we add the hint item to spinner paths
                paths.add("Pick a Member:");

                //refreshes Team member list for the spinner of users
                Call<ArrayList<String>> userArr = iMyService.getTeamMembers(loadEntryId());
                userArr.enqueue(new Callback<ArrayList<String>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                                        if (response.body() != null) {
                                            paths.addAll(response.body());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                users.setAdapter(adapter);

                //tags are handled here for tagTotal and TextView of tag list
                tagEntBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(tags.getText().toString().trim())){
                            Toast.makeText(getContext(), "MUST ENTER A TAG!", Toast.LENGTH_SHORT).show();
                            tags.setText("");
                            return;
                        }
                        tagTotal.add(tags.getText().toString().trim());
                        tagView.setText(String.join(", ", tagTotal));
                        tags.setText("");
                }
                });

                //handles deadline picking: inflates new View as well
                mveDatePick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View dateView = inflater.inflate(R.layout.date_picker_task, null);
                        builder.setTitle("Pick a Date");
                        //set methods to implement date changes here:
                        DatePicker datePicked = (DatePicker) dateView.findViewById(R.id.datePickerTask);

                        builder.setPositiveButton("Set Deadline", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dateDisplay.setText(datePicked.getDayOfMonth() + "/" + datePicked.getMonth() + "/" + datePicked.getYear());
                            }
                        });
                        builder.setNegativeButton("Anytime", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dateDisplay.setText("ANYTIME");
                            }
                        });

                        builder.setView(dateView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

            //Posting task starts here:
                builder.setPositiveButton("Create Task", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.isEmpty(title.getText().toString()) || users.getSelectedItem().toString().equalsIgnoreCase(adapter.getItem(0))){
                            Toast.makeText(getContext(), "MUST HAVE A TITLE!", Toast.LENGTH_SHORT).show();
                        }
                        else{ //enter in task creation block here
                            String checkDate = dateDisplay.getText().toString();
                            if(!checkDate.contains("/")){
                                checkDate =  "ANYTIME";
                            }
                            compositeDisposable.add(iMyService.postTask(loadEmail(), loadEntryId(), descr.getText().toString().trim(), title.getText().toString().trim(),
                                    checkDate, users.getSelectedItem().toString(), String.join(", ", tagTotal))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String response) throws Exception {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                    refreshTasks();
                                }
                            }));
                        }
                        compositeDisposable.clear();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //displays popup box
                builder.setView(dialogview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
    });


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
        compositeDisposable.clear();
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
