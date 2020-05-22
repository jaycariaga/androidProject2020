package com.example.grouporganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
    }

    //function called for the send button
    public void checkLogin(View view){
        Intent intent = new Intent(this, Home_page.class);
        EditText user = (EditText) findViewById(R.id.username);
        String checkuser = user.getText().toString();
        EditText pass = (EditText) findViewById(R.id.password);
        String checkpass = pass.getText().toString();
        System.out.println(checkpass + checkuser);
        //if(checkuser == "jason" && checkpass == "c"){

        //    startActivity(intent);
        //}
        startActivity(intent);


    }



}
