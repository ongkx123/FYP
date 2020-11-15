package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowAllBookingActivity extends AppCompatActivity {

    DatabaseReference ref,ref1;
    ArrayList<Payment> displayAllArrayList;
    RecyclerView recyclerView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_booking);

        if(getIntent().hasExtra("userName")){
            userName = getIntent().getStringExtra("userName");
        }

        ref = FirebaseDatabase.getInstance().getReference().child("Payment");
        setContentView(R.layout.activity_show_all_booking);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayAllArrayList = new ArrayList<>();

    }

    protected void onStart() {
        super.onStart();

        displayAllArrayList.clear();
        //Read and listen for changes to the entire contents of a path.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //read all the children that belong to above parent
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //pass data to payment class
                    Payment p = snapshot1.getValue(Payment.class);
                    //check the booking belong to this user
                    if(p.getUserID().equals(userName)){
                        displayAllArrayList.add(p);
                    }
                }
                ShowAllAdapter sad = new ShowAllAdapter(ShowAllBookingActivity.this,displayAllArrayList,userName);
                recyclerView.setAdapter(sad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowAllBookingActivity.this, "Wrong database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}