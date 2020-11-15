package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.ShowAllHolder> {

    Context context;
    ArrayList<Payment> displayAllArrayList;
    String userName;

    public ShowAllAdapter(Context context,ArrayList<Payment>displayAllArrayList,String userName){
        this.context=context;
        this.displayAllArrayList=displayAllArrayList;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ShowAllHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowAllHolder(LayoutInflater.from(context).inflate(R.layout.display_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAllHolder holder, final int position) {

        holder.location.setText(displayAllArrayList.get(position).getLocation());
        holder.date.setText(displayAllArrayList.get(position).getDate());
        holder.time.setText(displayAllArrayList.get(position).getStartTime()+"-"+displayAllArrayList.get(position).getEndTime());

        holder.l.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayBookingDetailsActivity.class);
                intent.putExtra("bookingID",displayAllArrayList.get(position).getId());
                intent.putExtra("userName",userName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return displayAllArrayList.size();
    }

    class ShowAllHolder extends RecyclerView.ViewHolder{

        TextView name,location,date,time;
        LinearLayout l;

        public ShowAllHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.tvLocation);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
            l = itemView.findViewById(R.id.LinearLayoutSearch);
        }
    }
}
