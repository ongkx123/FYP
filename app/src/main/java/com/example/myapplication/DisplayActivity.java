package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DisplayActivity extends AppCompatActivity {

    String name;
    DatabaseReference ref,ref1,ref2,ref3;
    TextView cName,cPhoneNo,cLocation;
    ImageView image,phone;
    String phoneNumber;
    Uri pN;
    Button navigation,booking,onlineMessage;
    String url;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        //Get the data from the previous activity
        if(getIntent().hasExtra("name") && getIntent().hasExtra("userName")){
            name = getIntent().getStringExtra("name");
            userName = getIntent().getStringExtra("userName");
        }

        //Get database reference
        ref = FirebaseDatabase.getInstance().getReference().child("Display");
        ref1 = ref.child(name);

        //Variable declaration
        cName = (TextView)findViewById(R.id.textViewDisplayName2);
        cLocation = (TextView)findViewById(R.id.textViewDisplayAddress);
        cPhoneNo = (TextView)findViewById(R.id.textViewDisplayPhone);
        image = (ImageView)findViewById(R.id.imageViewDisplay);
        phone = (ImageView)findViewById(R.id.imageViewPhone);
        navigation = (Button)findViewById(R.id.buttonNavigation);
        booking = (Button)findViewById(R.id.buttonBooking);
        onlineMessage = (Button)findViewById(R.id.buttonOnlineMessage);

        //Read and listen for changes to the entire contents of a path.
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //Pass the value to the Display Class
                    Display d = snapshot.getValue(Display.class);
                    //Set the title as the sport centre name
                    cName.setText(name);
                   //get the details of the sport centre that needed to be display
                    cLocation.setText(d.getAddress());
                    cPhoneNo.setText(d.getPhoneNo());
                    phoneNumber = "tel:"+d.getPhoneNo();
                    pN = Uri.parse(phoneNumber);
                    url = d.getUrl();
                    //put the image to the image view
                    Picasso.get().load(url).into(image);

                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT < 23) {
                            phoneCall(phoneNumber);
                        }else {

                            if (ActivityCompat.checkSelfPermission(DisplayActivity.this,
                                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                                phoneCall(phoneNumber);
                            }else {
                                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                                //Asking request Permissions
                                ActivityCompat.requestPermissions(DisplayActivity.this, PERMISSIONS_STORAGE, 9);
                            }
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Intent to the booking page
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DisplayActivity.this, BookingActivity.class);
                intent1.putExtra("name",name);
                intent1.putExtra("url",url);
                intent1.putExtra("userName",userName);
                startActivity(intent1);
            }
        });
        //Intent to the navigation page page
        navigation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DisplayActivity.this,ShowMapActivity.class);
                intent2.putExtra("name",name);
                startActivity(intent2);
            }
        });
        //Intent to the chat page
        onlineMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(DisplayActivity.this,ChatActivity.class);
                intent3.putExtra("name",name);
                intent3.putExtra("userName",userName);
                startActivity(intent3);
            }
        });


    }

    public double getCount(String c){

        double count;

        try {
            count = Double.parseDouble(c);
            return count;
        }catch (NumberFormatException ex){
            return 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall(phoneNumber);
        }else {
            Toast.makeText(DisplayActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(String phoneNumber){
        if (ActivityCompat.checkSelfPermission(DisplayActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(phoneNumber));
            DisplayActivity.this.startActivity(callIntent);
        }else{
            Toast.makeText(DisplayActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}