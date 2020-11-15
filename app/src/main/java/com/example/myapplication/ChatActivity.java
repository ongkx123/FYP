package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageButton imageButton;
    EditText editTextSend;
    TextView userName;
    String receiver;
    List<Chat> mchat;
    RecyclerView recyclerView;
    String sender;
    int count;
    DatabaseReference scReference,imageReference;
    private NotificationHelper mNotificationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //read data from previous activity
        if(getIntent().hasExtra("name") &&  getIntent().hasExtra("userName")){
            receiver = getIntent().getStringExtra("name");
            sender = getIntent().getStringExtra("userName");
        }

        recyclerView =(RecyclerView)findViewById(R.id.recycleViewChat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userName = (TextView)findViewById(R.id.chatUserName);
        editTextSend = (EditText)findViewById(R.id.text_send);
        imageButton = (ImageButton)findViewById(R.id.btn_send);
        
        userName.setText(receiver);

        String image = receiver;

        imageReference = FirebaseDatabase.getInstance().getReference().child("Display");
        scReference =imageReference.child(image);

        //Read and listen for changes to the entire contents of a path.
        scReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.child("url").getValue().toString();
                readMessage(sender,receiver,url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //send button clickable listener
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = editTextSend.getText().toString();
                //check whether the message is empty a not
                if(!message.equals("")){
                    String status = "false";
                    sendMessage(sender,receiver,message,status);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChatBot");
                    count = 0;
                    //Read and listen for changes to the entire contents of a path.
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                //get the keyword and message
                                String keyword = snapshot1.child("keyword").getValue().toString();
                                String message1 = snapshot1.child("message").getValue().toString();

                                if(count<1) {
                                    // check whether the message contain the keyword
                                    if (message.toLowerCase().contains(keyword)) {
                                        String status1 = "false";
                                        sendMessage(receiver, sender, message1,status1);
                                        count++;

                                        scReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String url = snapshot.child("url").getValue().toString();
                                                readMessage(sender,receiver,url);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                else{
                    Toast.makeText(ChatActivity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                }
                editTextSend.setText("");
            }
        });
    }

    //send message to database
    private void sendMessage(final String sender, final String receiver, final String message, String status){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat");
        String id = reference.push().getKey();
        Chat chat = new Chat(sender,receiver,message,status);

        reference.child(id).setValue(chat);

    }

    //read message from database
    private void readMessage(final String sender, final String receiverName,final String url){
        mchat = new ArrayList<>();

        final DatabaseReference reference1;

        reference1 = FirebaseDatabase.getInstance().getReference().child("Chat");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //pass data into the Chat class
                    Chat chat = snapshot1.getValue(Chat.class);

                    //check whether it is the latest message
                    if(chat.getSender().equals(receiverName) && chat.getReceiver().equals(sender) && chat.getStatus().equals("true")){
                        //get the key
                        String id = snapshot1.getKey();
                        String message = snapshot1.child("message").getValue().toString();
                        reference1.child(id).child("status").setValue("false");

                        //show notification
                        mNotificationHelper = new NotificationHelper(ChatActivity.this);
                        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(receiver,message,sender);
                        mNotificationHelper.getManager().notify(1,nb.build());
                    }
                    // check whether the message is belonging to this user and the receiver
                    if(chat.getReceiver().equals(sender) && chat.getSender().equals(receiverName) || chat.getReceiver().equals(receiverName) && chat.getSender().equals(sender )){
                        mchat.add(chat);
                    }
                    MessageAdapter messageAdapter = new MessageAdapter(ChatActivity.this,mchat,url,sender);
                    recyclerView.setAdapter(messageAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}