package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeHolder> {

    Context context;
    ArrayList<Type> typeArrayList;
    String userName;

    public TypeAdapter(Context context,ArrayList<Type> typeArrayList,String userName){
        this.context=context;
        this.typeArrayList=typeArrayList;
        this.userName=userName;
    }

    @NonNull
    @Override
    public TypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeHolder(LayoutInflater.from(context).inflate(R.layout.type_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TypeHolder holder, final int position) {
        holder.name.setText(typeArrayList.get(position).getName());
        Picasso.get().load(typeArrayList.get(position).getImage()).resize(1200,400).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SearchActivity.class);
                intent.putExtra("name",typeArrayList.get(position).getName());
                intent.putExtra("userName",userName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return typeArrayList.size();
    }

    class TypeHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView image;

        public TypeHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewType);
            image = itemView.findViewById(R.id.imageViewType);
        }
    }
}
