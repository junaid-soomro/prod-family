package com.example.dark.prod_family_project.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.dark.prod_family_project.Models.*;
import com.example.dark.prod_family_project.R;

import java.util.ArrayList;

/**
 * Created by abd on 18-Jan-18.
 */

public class userlistfetch extends RecyclerView.Adapter<userlistfetch.myViewHolder> {

    ArrayList<user_list> arrayList = new ArrayList<>();

    public userlistfetch(ArrayList<user_list> arrayList) {
        this.arrayList = arrayList;
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_users,parent,false);
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        holder.email.setText(arrayList.get(position).getEmail());
        holder.name.setText(arrayList.get(position).getName());
        holder.type.setText(arrayList.get(position).getUser_type());
        holder.status.setText(arrayList.get(position).getUser_status());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView name,email,type,status;

        public myViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.NAME);
            email  = (TextView)itemView.findViewById(R.id.EMAIL);
            type = (TextView)itemView.findViewById(R.id.TYPE);
            status = (TextView)itemView.findViewById(R.id.status);
        }
    }

}
