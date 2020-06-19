package com.example.grouporganizer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.Team;

import java.util.List;

public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.TeamsViewHolder> {
    private List<Team> mDataset;

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        TeamsViewHolder vh = new TeamsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {
        holder.textView.setText(mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class TeamsViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TeamsViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public TeamsListAdapter(List<Team> dataset) {

        mDataset = dataset;
    }



}
