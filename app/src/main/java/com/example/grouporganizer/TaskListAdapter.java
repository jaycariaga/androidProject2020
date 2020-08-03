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

import com.example.grouporganizer.Retrofit.Task;

import org.w3c.dom.Text;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>{

    private List<Task> mDataset;

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskListAdapter.TaskViewHolder vh = new TaskListAdapter.TaskViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        ShapeDrawable sd = new ShapeDrawable();
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.BLACK);
        sd.getPaint().setStrokeWidth(7f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        //holder.textView.setBackground(sd);
        holder.title.setTextColor(Color.BLUE);

        holder.deadline.setText(mDataset.get(position).getDeadline());
        holder.title.setText(mDataset.get(position).getTitle());
        holder.TaskCrd.setBackground(sd);
        holder.descrip.setText(mDataset.get(position).getDescription());
        holder.tagList.setText(mDataset.get(position).getTags());
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView deadline;
        public RelativeLayout TaskCrd;
        public TextView descrip;
        public TextView tagList;
        public TaskViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tskTitle);
            deadline = v.findViewById(R.id.tskDedlne);
            TaskCrd = v.findViewById(R.id.tskView);
            descrip = v.findViewById(R.id.tskDscr);
            tagList = v.findViewById(R.id.tsk_Tags);

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public TaskListAdapter(List<Task> dataset) {
        mDataset = dataset;
    }



}
