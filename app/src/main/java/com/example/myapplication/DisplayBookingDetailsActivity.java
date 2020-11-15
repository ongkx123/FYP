package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DisplayBookingDetailsActivity extends AppCompatActivity {

    String id;
    DatabaseReference ref,ref1;
    String dName,dDate,dTime,dNoCourt,dPrice,dMethod,dLocation;
    String location,url;
    TextView tName,tDate,tNoCourt,tPrice,tMethod,tTime;
    ImageView image;
    DatabaseReference displayRef,scRef;
    Button btmUpdate,btmDelete;
    Button buttonCon;
    ImageView imageClose;
    Dialog dialog;
    String startTime,endTime,noCourt,name,date,address;
    DatabaseReference ref2,ref3,ref4,ref5,ref6,ref7;
    int sTime,eTime,nc;
    int m=0;
    int testTime= 1000;
    String userName;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_payment);

        if(getIntent().hasExtra("bookingID") && getIntent().hasExtra("userName")){
            id= getIntent().getStringExtra("bookingID");
        }

        tName = (TextView)findViewById(R.id.textViewName2);
        tDate = (TextView)findViewById(R.id.textViewDate2);
        tTime = (TextView)findViewById(R.id.textViewTime2);
        tNoCourt = (TextView)findViewById(R.id.textViewNoCourt2);
        tPrice = (TextView)findViewById(R.id.textViewPaymentPrice);
        tMethod = (TextView)findViewById(R.id.textViewPaymentMethod);
        btmUpdate = (Button)findViewById(R.id.buttonUpdate);
        btmDelete = (Button)findViewById(R.id.buttonDelete);
        buttonCon = (Button)findViewById(R.id.buttonCon);
        image = (ImageView)findViewById(R.id.imageViewDisplayImage);
        home = (ImageView)findViewById(R.id.imageViewHome);

        dialog = new Dialog(this);


        ref = FirebaseDatabase.getInstance().getReference().child("Payment");

        //Read and listen for changes to the entire contents of a path.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    //Pass data to payment class
                    Payment p = snapshot1.getValue(Payment.class);
                    //check the payment id
                    if(p.getId().equals(id)){
                          tName.setText(p.getName());
                          tDate.setText(p.getDate());
                          tTime.setText(p.getStartTime()+"-"+p.getEndTime());
                          tNoCourt.setText(p.getNoCourt());
                          tPrice.setText(p.getTotalPrice());
                          tMethod.setText(p.getPaymentStatus());
                          location = p.getLocation();

                        //Read and listen for changes to the entire contents of a path.
                          displayRef = FirebaseDatabase.getInstance().getReference().child("Display");
                          scRef = displayRef.child(location);
                          scRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String url = snapshot.child("url").getValue().toString();
                                //set image
                                Picasso.get().load(url).into(image);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayBookingDetailsActivity.this,UpdateBookingActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        btmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DisplayBookingDetailsActivity.this,UpdateBookingActivity.class);
                intent1.putExtra("bookingID",id);
                intent1.putExtra("userName",userName);
                startActivity(intent1);
            }
        });


        //Delete the booking activity
        btmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //display a warning dialog
                dialog.setContentView(R.layout.warning_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                imageClose = dialog.findViewById(R.id.imageViewClose);
                buttonCon = dialog.findViewById(R.id.buttonCon);
                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //confirm button listener on the dialog
                buttonCon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        readData(new FirebaseCallBack() {
                            @Override
                            public void onCallback(String s, String e, String n,String a,String d,String na) {

                                startTime = s;
                                endTime = e;
                                noCourt = n;
                                address = a;
                                date = d;
                                name=na;

                                ref2 = FirebaseDatabase.getInstance().getReference().child("SportCentre");
                                ref3 = ref2.child(address);
                                ref4 = ref3.child(date);


                                sTime = Count(startTime);
                                eTime = Count(endTime);
                                nc = Count(noCourt);

                                //Read and listen for changes to the entire contents of a path.
                                ref4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                            if (testTime < sTime) {
                                                testTime = testTime + 100;
                                            } else {
                                                if (m <= ((eTime - sTime) / 100) * nc) {
                                                    if (sTime < eTime) {
                                                        int countCourt1 = 1, noCourt1 = 1;
                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                            BookingStatus bs = dataSnapshot2.getValue(BookingStatus.class);
                                                            if (countCourt1 <= nc && noCourt1 <= 4) {
                                                                //check whether the booking is belonging to user
                                                                if (bs.getStatus().equals("Yes") && bs.getName().equals(name)) {
                                                                    String bookingStatus = "No";
                                                                    String cNumber = "Court" + noCourt1;
                                                                    String bookName = "";
                                                                    BookingStatus bs1 = new BookingStatus(cNumber, bookName, bookingStatus);

                                                                    String st = String.valueOf(sTime);
                                                                    ref5 = ref4.child(st);
                                                                    ref5.child(cNumber).setValue(bs1);
                                                                    m++;
                                                                    countCourt1++;
                                                                }
                                                                noCourt1++;
                                                            }
                                                        }
                                                    }
                                                    sTime = sTime + 100;
                                                    testTime = testTime + 100;
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        ref6 = FirebaseDatabase.getInstance().getReference().child("Payment");
                        ref7 = ref6.child(id);
                        ref7.removeValue();

                        dialog.dismiss();
                        Intent intent = new Intent(DisplayBookingDetailsActivity.this,LocationSearchBookingActivity.class);
                        intent.putExtra("userName",userName);
                        startActivity(intent);
                    }

                });



                dialog.show();

            }
        });



    }

    private void readData(final FirebaseCallBack firebaseCallBack){

        ref1 = FirebaseDatabase.getInstance().getReference().child("Payment");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    Payment pay = snapshot1.getValue(Payment.class);
                    if(pay.getId().equals(id)){
                        startTime = pay.getStartTime();
                        endTime = pay.getEndTime();
                        noCourt = pay.getNoCourt();
                        address = pay.getLocation();
                        date = pay.getDate();
                        name = pay.getName();


                    }
                }
                firebaseCallBack.onCallback(startTime,endTime,noCourt,address,date,name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private interface FirebaseCallBack{
        void onCallback(String s, String e,String n, String a,String d,String na);
    }

    public int Count(String s){

        int num;

        try {
            num = Integer.parseInt(s);
            return num;
        }catch (NumberFormatException ex){
            return 0;
        }
    }
}