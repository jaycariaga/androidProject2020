package com.example.grouporganizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Home_page extends AppCompatActivity {
    FragmentManager fm;
    FragmentTeamsList fragmentTeamsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        fm = getSupportFragmentManager();
        fragmentTeamsList = new FragmentTeamsList();
        fm.beginTransaction().add(R.id.fragment_container, fragmentTeamsList).commit();

        final CoordinatorLayout root = findViewById(R.id.homepage_root);

        final FloatingActionButton fab = findViewById(R.id.homepage_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, new FragmentRegisterTeam());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fm.findFragmentById(R.id.fragment_container) instanceof FragmentTeamsList) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }

            }
        });

        //function below just checks success on cache
        //LoadPreferences();
    }



    //loads name of the logged in user: meant for fetching email of current user for reference
    private String LoadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String  data = sharedPreferences.getString("email", "") ;
        Toast.makeText(this,data, Toast.LENGTH_LONG).show();

        return data;
    }


}
