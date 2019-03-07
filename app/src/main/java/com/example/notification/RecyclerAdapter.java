package com.example.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Users> list;
    private Context context;
    public RecyclerAdapter(Context context,List<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
          final String user_name = list.get(i).getName();
           viewHolder.username.setText(user_name);

           Glide.with(context).load(list.get(i).getImage()).into(viewHolder.circleImageView);
           final String user_Id = list.get(i).userId;

           viewHolder.view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent sendIntent = new Intent(context,SendActivity.class);
                   sendIntent.putExtra("key",user_Id);
                   sendIntent.putExtra("name",user_name);
                   context.startActivity(sendIntent);
               }
           });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private CircleImageView circleImageView;
        private TextView username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            circleImageView = view.findViewById(R.id.listImage);
            username = view.findViewById(R.id.listName);
        }
    }
}
