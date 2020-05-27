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


    public void SignUpMove(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    //function called for the send button
    public void checkLogin(View view){
        Intent intent = new Intent(this, Home_page.class);
        EditText user = (EditText) findViewById(R.id.email);
        String checkuser = user.getText().toString();
        EditText pass = (EditText) findViewById(R.id.password);
        String checkpass = pass.getText().toString();
        System.out.println(checkuser + checkpass);
        if(checkuser.equals("jason") && checkpass.equals("c")){

            startActivity(intent);
        }
        else{
            System.out.println("Wrong Login:");
        }//startActivity(intent);
    }



}
