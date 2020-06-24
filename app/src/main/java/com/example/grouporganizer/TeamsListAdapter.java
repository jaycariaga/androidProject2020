package com.example.grouporganizer;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grouporganizer.Retrofit.Team;

import java.util.List;

public class TeamsListAdapter extends RecyclerView.Adapter<TeamsListAdapter.TeamsViewHolder> {
    private List<Team> mDataset;
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        TeamsViewHolder vh = new TeamsViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {
        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(1f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        holder.textView.setBackground(sd);
        holder.textView.setTextColor(Color.WHITE);
        holder.textView.setText(mDataset.get(position).getName());
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
        public TextView textView;
        public TextView AdminCheckView;
        public TeamsViewHolder(TextView v, final OnItemClickListener listener) {
            super(v);
            textView = v;
            //for on click method
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public TeamsListAdapter(List<Team> dataset) {
        mDataset = dataset;
    }



}
