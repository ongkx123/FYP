package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LocationSearchBookingActivity extends AppCompatActivity {

    LinearLayout ly1,ly2,ly3;
    String userName;
    TextView uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search_booking);

        if(getIntent().hasExtra("userName")){
            userName = getIntent().getStringExtra("userName");
        }

        ly1 = (LinearLayout)findViewById(R.id.LinearLayoutFirst);
        ly2 = (LinearLayout)findViewById(R.id.LinearLayoutSecond);
        ly3 = (LinearLayout)findViewById(R.id.LinearLayoutThird);
        uName = (TextView)findViewById(R.id.textViewHiUser);
        uName.setText("Welcome Back "+userName);



        ly1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LocationSearchBookingActivity.this,Type_Activity.class);
                intent1.putExtra("userName",userName);
                startActivity(intent1);
            }
        });

        ly2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LocationSearchBookingActivity.this,ShowAllBookingActivity.class);
                intent2.putExtra("userName",userName);
                startActivity(intent2);
            }
        });

        ly3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(LocationSearchBookingActivity.this,ChatListActivity.class);
                intent3.putExtra("userName",userName);
                startActivity(intent3);
            }
        });
    }
}