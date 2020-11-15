package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Type_Activity extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Type> typeArrayList;
    RecyclerView recyclerView;
    SearchView searchView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        if(getIntent().hasExtra("userName")){
            userName = getIntent().getStringExtra("userName");
        }

        ref = FirebaseDatabase.getInstance().getReference().child("Type");
        setContentView(R.layout.activity_type);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeArrayList = new ArrayList<>();
        searchView = (SearchView) findViewById(R.id.searchViewType);
    }

    protected void onStart() {

        super.onStart();

        typeArrayList.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Type t = snapshot1.getValue(Type.class);
                    typeArrayList.add(t);
                }
                TypeAdapter ta = new TypeAdapter(Type_Activity.this,typeArrayList,userName);
                recyclerView.setAdapter(ta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Type_Activity.this, "Wrong database", Toast.LENGTH_SHORT).show();
            }
        });

        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String s){
        ArrayList<Type> typeArrayList1 = new ArrayList<>();
        for(Type object : typeArrayList){
            if(object.getName().toLowerCase().contains(s.toLowerCase())){
                typeArrayList1.add(object);
            }
        }
        TypeAdapter tp = new TypeAdapter(Type_Activity.this,typeArrayList1,userName);
        recyclerView.setAdapter(tp);
    }
}