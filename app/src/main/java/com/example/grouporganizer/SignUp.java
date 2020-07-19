package com.example.grouporganizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import org.w3c.dom.Text;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SignUp extends AppCompatActivity {

    //creating object dump and interface for API express services
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    } //onStop needed by CompositeDisposable to destroy unneeded objects


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //additions to initialize api services
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //route back to login page
        TextView loginlink = findViewById(R.id.LoginSwitch);
        loginlink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });

    }

    public void checkSignUpDetail(View view){
//reads inputs and converts to readable string
        EditText user = findViewById(R.id.email);
        String newemail = user.getText().toString();
        EditText pass = findViewById(R.id.password);
        String checkpass = pass.getText().toString();
        EditText name = findViewById(R.id.firstname);
        String newname = name.getText().toString();

        //make sure the string readable inputs are NOT EMPTY! WORKS and displays on bottom of app
        if(TextUtils.isEmpty(newemail) || TextUtils.isEmpty(checkpass) || TextUtils.isEmpty(newname)){
            Toast.makeText(this, "Must fill ALL fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //composite Disposable destroys data that isn't helpful anymore (ex. unsuccessful logins)
        compositeDisposable.add(iMyService.registerUser(newemail, newname, checkpass).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(SignUp.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                    }
                }));


    }

}
