package com.example.grouporganizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.IMyService;
import com.example.grouporganizer.Retrofit.RetrofitClient;
import com.example.grouporganizer.Retrofit.Team;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private String result; //for checkAdmin() method

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        TeamsViewHolder vh = new TeamsViewHolder(v, mListener);
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
        //System.out.println("this is a " + check);
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
        void onItemClick(int position);
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
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
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
    //doesnt work yet needs to get admin choice inside of it
    @SuppressLint("CheckResult")
    private Boolean getAdminCheck(String entryID){

        //result = "";
        System.out.println(entryID);
        Observable<String> response = iMyService.checkAdmin(currentUser, entryID);
        response.subscribe(s ->
            result = s
        );
        System.out.println(result);

        if(result.equals("0"))
            return false;
        return true;


    }





}
