package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder>{

    Context context;
    ArrayList<Chat> cList;
    String name;

    public ChatListAdapter(Context context, ArrayList<Chat> cList,String name){
        this.context = context;
        this.cList = cList;
        this.name=name;
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListHolder(LayoutInflater.from(context).inflate(R.layout.chat_list_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, final int position) {
        if(cList.get(position).getReceiver().equals(name)){
            holder.name.setText(cList.get(position).getSender());
        }
        else {
            holder.name.setText(cList.get(position).getReceiver());
        }

        holder.cl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("userName",name);
                if(cList.get(position).getReceiver().equals(name)){
                    intent.putExtra("name",cList.get(position).sender);
                }
                else {
                    intent.putExtra("name",cList.get(position).receiver);
                }

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    class ChatListHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView img;
        ConstraintLayout cl;

        public ChatListHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textViewSenderName);
            img = itemView.findViewById(R.id.profileIcon);
            cl = itemView.findViewById(R.id.constraintLayoutChat);
        }
    }
}
