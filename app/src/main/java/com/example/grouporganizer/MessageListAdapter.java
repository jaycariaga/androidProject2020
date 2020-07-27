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

import com.example.grouporganizer.Retrofit.Messages;

import java.util.List;

//TODO: Meant to be the adapter in teams page that loads up messages (very similar to teamslistadapter)
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<Messages> mDataset;

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        MessageListAdapter.MessageViewHolder vh = new MessageListAdapter.MessageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(7f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        //holder.textView.setBackground(sd);
        holder.textView.setTextColor(Color.BLUE);

        holder.dateView.setText(mDataset.get(position).getTimestamp());
        holder.textView.setText(mDataset.get(position).getGenMsg());
        holder.MsgCard.setBackground(sd);
        holder.UserMsg.setText(mDataset.get(position).getSender());
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView dateView;
        public RelativeLayout MsgCard;
        public TextView UserMsg;
        public MessageViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.msgtxt);
            dateView = v.findViewById(R.id.msgDate);
            MsgCard = v.findViewById(R.id.MessageCardView);
            UserMsg = v.findViewById(R.id.msgUser);
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
