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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    Context context;
    ArrayList<DisplaySportCentre> displaySportCentres;
    String userName;

    public SearchAdapter(Context context,ArrayList<DisplaySportCentre>displaySportCentres,String userName){
        this.context=context;
        this.displaySportCentres=displaySportCentres;
        this.userName=userName;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHolder(LayoutInflater.from(context).inflate(R.layout.search_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, final int position) {

        holder.name.setText(displaySportCentres.get(position).getName());
        holder.location.setText(displaySportCentres.get(position).getLocation());
        holder.meter.setText(displaySportCentres.get(position).getMeter());

        holder.ly.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DisplayActivity.class);
                intent.putExtra("name",displaySportCentres.get(position).getName());
                intent.putExtra("userName",userName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return displaySportCentres.size();
    }

    class SearchHolder extends RecyclerView.ViewHolder{
        TextView name,location,meter;
        LinearLayout ly;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.textViewName2);
            location = (TextView) itemView.findViewById(R.id.textViewLocation);
            meter = (TextView) itemView.findViewById(R.id.textViewMeter);
            ly = (LinearLayout) itemView.findViewById(R.id.LinearLayoutSearch);
        }
    }
}
