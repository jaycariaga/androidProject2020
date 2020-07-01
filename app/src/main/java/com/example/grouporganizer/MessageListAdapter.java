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

import com.example.grouporganizer.Retrofit.Messages;

import java.util.List;

//TODO: Meant to be the adapter in teams page that loads up messages (very similar to teamslistadapter)
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<Messages> mDataset;

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        MessageListAdapter.MessageViewHolder vh = new MessageListAdapter.MessageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(1f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        holder.textView.setBackground(sd);
        holder.textView.setTextColor(Color.WHITE);
        holder.textView.setText(mDataset.get(position).getGenMsg());
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView AdminCheckView;
        public MessageViewHolder(TextView v) {
            super(v);
            textView = v;
            //for on click method
            //itemView.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        if(listener != null){
            //            int position = getAdapterPosition();
            //            if(position != RecyclerView.NO_POSITION){
            //                listener.onItemClick(position);
            //           }
            //      }
            //    }
            //});
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public MessageListAdapter(List<Messages> dataset) {
        mDataset = dataset;
    }


}
