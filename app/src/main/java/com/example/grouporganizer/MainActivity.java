package com.example.grouporganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//LOGIN PAGE

public class MainActivity extends AppCompatActivity {
    LinearLayout root;
    ProgressBar loginIndicator;

    //creating object dump and interface for API express services
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    boolean coldStart = true;

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

        root = findViewById(R.id.login_root);
        loginIndicator = findViewById(R.id.login_indicator);


        //additions to initialize api services
        while(true) {
            try {
                Retrofit retrofitClient = RetrofitClient.getInstance();
                iMyService = retrofitClient.create(IMyService.class);

                authenticateToken();
                return;
            }
            catch (Exception e) {root.setVisibility(View.VISIBLE);
        loginIndicator.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                throw e;
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!coldStart) {
            root.setVisibility(View.VISIBLE);
            loginIndicator.setVisibility(View.GONE);
        }
        coldStart = false;
    }

    public void authenticateToken() {
        root.setVisibility(View.GONE);
        loginIndicator.setVisibility(View.VISIBLE);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String jwt = sharedPref.getString("jwt", null);
        if (jwt != null) {
            Call<String> call = iMyService.authenticateToken(jwt);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body().equals("valid")) {
                        startActivity(new Intent(MainActivity.this, Home_page.class));
                    } else {
                        root.setVisibility(View.VISIBLE);
                        loginIndicator.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    root.setVisibility(View.VISIBLE);
                    loginIndicator.setVisibility(View.GONE);
                }
            });
        } else {
            root.setVisibility(View.VISIBLE);
            loginIndicator.setVisibility(View.GONE);
        }

    }

    //simple button that moves to registration
    public void SignUpMove(View view){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    //function called for the send button and conducts login credentials and local user handling (WORKS)
    public void checkLogin(View view) throws Exception{
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

        //saves local cache of user for use post login
        SavePreferences(checkuser);

        try {
            //composite Disposable destroys data that isn't helpful anymore (ex. unsuccessful logins)
            compositeDisposable.add(iMyService.loginUser(checkuser, checkpass).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            JWT jwt = new JWT(response);
                            getPreferences(MODE_PRIVATE).edit().putString("jwt", jwt.toString()).apply();
                            startActivity(new Intent(MainActivity.this, Home_page.class));
                            //moves state if success
                        }

                    }));
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this, "Server not connected. Try Again", Toast.LENGTH_SHORT).show();
        }

    } //finishes login function

    private void SavePreferences(String email){
        //local storage of currently logged in user...
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.commit();
        System.out.println(email);
    }

} //end of Main Activity
