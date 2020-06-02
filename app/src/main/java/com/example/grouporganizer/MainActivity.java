package com.example.grouporganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

//LOGIN PAGE

public class MainActivity extends AppCompatActivity {

    //creating object dump and interface for API express services
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    } //onStop needed by CompositeDisposable to destroy unneeded objects

//beginning method...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        //additions to initialize api services
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

    }

    //simple button that moves to registration
    public void SignUpMove(View view){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    //function called for the send button (WORKS)
    public void checkLogin(View view){
        //reads inputs and converts to readable string
        EditText user = (EditText) findViewById(R.id.email);
        String checkuser = user.getText().toString();
        EditText pass = (EditText) findViewById(R.id.password);
        String checkpass = pass.getText().toString();

        //make sure the string readable inputs are NOT EMPTY! WORKS and displays on bottom of app
        if(TextUtils.isEmpty(checkuser) || TextUtils.isEmpty(checkpass)){
            Toast.makeText(this, "Must fill ALL fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //composite Disposable destroys data that isn't helpful anymore (ex. unsuccessful logins)
        compositeDisposable.add(iMyService.loginUser(checkuser, checkpass).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String response) throws Exception {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                setContentView(R.layout.homepage);
            }
        }));




    } //finishes login function



} //end of Main Activity
