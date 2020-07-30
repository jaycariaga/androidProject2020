package com.example.grouporganizer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.Messages;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageFragment extends Fragment {
    List<Messages> db;
    RecyclerView rv;
    ImageButton buttonSend;
    EditText editTextMessage;
    MessageListAdapter adapter;
    Retrofit retrofit = RetrofitClient.getInstance();
    IMyService iMyService = retrofit.create(IMyService.class);
    String teamID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_message, container, false);
        buttonSend = v.findViewById(R.id.button_message_send);
        editTextMessage = v.findViewById(R.id.edit_text_message_create);

        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(7f);
        sd.getPaint().setStyle(Paint.Style.STROKE);
        editTextMessage.setBackground(sd);


        db = new ArrayList<>();
        rv = v.findViewById(R.id.message_list);
        adapter = new MessageListAdapter(db);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        teamID = getArguments().getString("entryId");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(editTextMessage.getText().toString().trim())) {
                    //attempt to load cached email into string data - retrieved from successful login
                    iMyService.sendgenmsg(loadEmail(), editTextMessage.getText().toString(), loadEntryId(), new Date()).enqueue(new Callback<List<Messages>>() {
                        @Override
                        public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                            refreshMessages();
                        }

                        @Override
                        public void onFailure(Call<List<Messages>> call, Throwable t) {
                            refreshMessages();
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Message must have content", Toast.LENGTH_SHORT).show();
                }
                editTextMessage.setText("");

            }
        });

        refreshMessages();
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

    void refreshMessages() {
        //attempt to load cached email into string data - retrieved from successful login
        Call<List<Messages>> messagesCall = iMyService.getGenThreadLst(loadEntryId());
        messagesCall.enqueue(new Callback<List<Messages>>() {
            @Override
            public void onResponse(Call<List<Messages>> call, Response<List<Messages>> response) {
                if (response.body() != null) {
                    db.clear();
                    db.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Messages>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
