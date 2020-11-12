package com.example.instagramclone.Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;

public class storyAdapter extends RecyclerView.Adapter<storyAdapter.MyViewHolder> {

    Context context;
    String[] data1;
    int[] img;

    public storyAdapter(Context context, String[] data1, int[] img) {
        this.context = context;
        this.data1 = data1;
        this.img = img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater i = LayoutInflater.from(context);
        View v = i.inflate(R.layout.each_story,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userName.setText(data1[position]);
        holder.imageView.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            imageView = itemView.findViewById(R.id.storyImageView);
        }

    }
}
