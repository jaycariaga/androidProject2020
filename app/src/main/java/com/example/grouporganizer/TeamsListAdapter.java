package com.example.grouporganizer;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
        sd.getPaint().setStrokeWidth(1f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        holder.wholeItem.setBackground(sd);
        holder.teamName.setTextColor(Color.WHITE);
        holder.teamName.setText(mDataset.get(position).getName());
        holder.AdminCheckView.setText("deez nutz");
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

    public TeamsListAdapter(List<Team> dataset) {
        mDataset = dataset;
    }



}
