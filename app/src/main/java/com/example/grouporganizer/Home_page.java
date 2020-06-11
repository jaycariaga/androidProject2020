package com.example.grouporganizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        TeamPageFragment teamPageFragment = new TeamPageFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, teamPageFragment).commit();
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
