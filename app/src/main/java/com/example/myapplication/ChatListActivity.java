package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    DatabaseReference ref,ref2,ref3;
    RecyclerView recyclerView;
    ArrayList<Chat> chatArrayList;
    ArrayList<ChatList> chatListArrayList;
    String name;
    String receiver;
    String sender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //read data from previous activity
        if(getIntent().hasExtra("userName")){
            name = getIntent().getStringExtra("userName");
        }

        ref2 = FirebaseDatabase.getInstance().getReference().child("Display");
        ref3 = ref2.child("Lavana Sport Centre");
        ref = FirebaseDatabase.getInstance().getReference().child("Chat");
        setContentView(R.layout.activity_chat_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycleVIewChatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatArrayList = new ArrayList<>();
        chatListArrayList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Read and listen for changes to the entire contents of a path.
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //Pass data to the Chat class
                    Chat chat = snapshot1.getValue(Chat.class);
                    //Check whether the message belong to user
                    if(chat.getSender().equals(name) || chat.getReceiver().equals(name)){
                        receiver = chat.getReceiver();
                        sender = chat.getSender();
                        //check whether the receiver have show multiple time
                        if(chatArrayList.contains(receiver)||chatArrayList.contains(sender)){

                        }else {
                            chatArrayList.add(chat);
                        }
                    }
                }
                ChatListAdapter clAdapter = new ChatListAdapter(ChatListActivity.this,chatArrayList,name);
                recyclerView.setAdapter(clAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}