package com.example.grouporganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FragmentRegisterTeam extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_team, container, false);
        Retrofit retrofit = RetrofitClient.getInstance();
        final IMyService iMyService = retrofit.create(IMyService.class);


        Button button = v.findViewById(R.id.register_team_button);
        final EditText nameField = v.findViewById(R.id.register_team_name);
        final EditText emailField = v.findViewById(R.id.register_team_email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new CompositeDisposable().add(iMyService.teamRegister(emailField.getText().toString(), nameField.getText().toString()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                if(response.contains("success")) {
                                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                                } else {
                                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));
            }
        });
        return v;
    }
}
