package com.example.grouporganizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.JWT;
import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.TeamsViewHolder> {
    private List<Team> mDataset;
    private OnItemClickListener mListener;
    private String currentUser;
    Retrofit retrofit = RetrofitClient.getInstance();
    IMyService iMyService = retrofit.create(IMyService.class);
    //private String result; //for checkAdmin() method

    CompositeDisposable compositeDisposable = new CompositeDisposable();



    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        TeamsViewHolder vh = new TeamsViewHolder(v, mListener);
        //trying this out for the .execute() function
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {
        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(7f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        //handles admin/member text options
        Boolean check = false;
        check = getAdminCheck(mDataset.get(position).getEntryID());
        System.out.println("this is a " + check);
        if(check == true)
            holder.AdminCheckView.setText("Admin");
        else
            holder.AdminCheckView.setText("Member");

        holder.wholeItem.setBackground(sd);
        holder.teamName.setTextColor(Color.WHITE);
        holder.teamName.setText(mDataset.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //attempt to do item click
    public interface OnItemClickListener{
        void onItemClick(int position, String membership);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class TeamsViewHolder extends RecyclerView.ViewHolder {
        public TextView teamName;
        public RelativeLayout wholeItem;
        public TextView AdminCheckView;
        public TeamsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            teamName = itemView.findViewById(R.id.teamNameView);
            wholeItem = itemView.findViewById(R.id.teamRelativeView);
            AdminCheckView = itemView.findViewById(R.id.adminCheckView);
            //for on click method
            teamName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View itemView) {
                    if(listener != null){
                        //attempt to save admin roles upon clicking
                        String membership = AdminCheckView.getText().toString();
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position, membership);
                        }
                    }
                }
            });
        }
    }

    public TeamsListAdapter(List<Team> dataset, String user) {
        mDataset = dataset;
        currentUser = user;
    }

    //TODO:yea im just trying to make this function take the response of check Admin and send a string from it
    private Boolean getAdminCheck(String entryID){
        Integer result = 0;
        //System.out.println(entryID);
        Call<Integer> getans = iMyService.checkAdmin(currentUser, entryID);
        Response<Integer> response = null;
        try {
            response = getans.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = response.body();
        if(result == 0 || result == null)
            return false;
        return true;
    }


}
