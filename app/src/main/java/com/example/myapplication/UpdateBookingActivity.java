package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UpdateBookingActivity extends AppCompatActivity {

    TextView dateError,timeError,courtNoError;
    ImageView imageViewSportCentre;
    TextView textViewName,personName;
    EditText editTextDate;
    Spinner spStart,spEnd,spCourtNo;
    String startTime,endTime,courtNo,date;
    DatePickerDialog datePickerDialog;
    Button confirmBookingBtn;
    String id;
    DatabaseReference ref,ref1;
    String name,location,address,startTime1,endTime1,noCourt,date1,method,userID,totalPrice;
    double countTotalPrice;
    DatabaseReference paymentReference,paymentReference1;
    //DatabaseReference reference,sReference,dReference,tReference,ddRef;
    DatabaseReference tReference;
    DatabaseReference displayReference,displayReference1;
    int sTime,eTime,nc;
    int testTime=1000;
    int mCount=0;
    int calTime=1000;
    int k=0;
    int hour,totalHC;
    int sTime1,eTime1,nc1;
    String bookerName,courtLocation;
    String userName;
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        //get data from previous activity
        if(getIntent().hasExtra("bookingID") && getIntent().hasExtra("userName")){
            id= getIntent().getStringExtra("bookingID");
            userName = getIntent().getStringExtra("userName");

        }

        dateError = (TextView)findViewById(R.id.textViewDateError1);
        timeError = (TextView)findViewById(R.id.textViewTimeError1);
        courtNoError = (TextView)findViewById(R.id.textViewCourtError1);

        imageViewSportCentre = (ImageView)findViewById(R.id.imageViewSportCentre1);
        textViewName = (TextView)findViewById(R.id.textViewSportCentreName1);

        personName = findViewById(R.id.textPersonName1);
        editTextDate = (EditText) findViewById(R.id.editTextDate1);

        //get calander
        Calendar cal  = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        spStart = (Spinner)findViewById(R.id.spinnerStartTime1);
        spEnd = (Spinner)findViewById(R.id.spinnerEndTime1);
        spCourtNo = (Spinner)findViewById(R.id.spinnerCourtNo1);

        textViewName.setText("Update Booking");

        paymentReference = FirebaseDatabase.getInstance().getReference().child("Payment");
        paymentReference1 = paymentReference.child(id);


        //get the sport centre imange
        paymentReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot paymentSnapshot) {
                bookerName = paymentSnapshot.child("name").getValue().toString();

                //set sport centre name
                personName.setText(bookerName);
                courtLocation = paymentSnapshot.child("location").getValue().toString();

                displayReference=FirebaseDatabase.getInstance().getReference().child("Display");
                displayReference1=displayReference.child(courtLocation);

                displayReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot displaySnapshot) {
                        String url = displaySnapshot.child("url").getValue().toString();
                        //set image
                        Picasso.get().load(url).into(imageViewSportCentre);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set the spinner
        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(this,R.array.time, R.layout.color_spinner_layout);
        adapterTime.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        ArrayAdapter<CharSequence> adapterCourtNo = ArrayAdapter.createFromResource(this,R.array.courtNo, R.layout.color_spinner_layout);
        adapterCourtNo.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spStart.setAdapter(adapterTime);
        spStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get data from spinner
                startTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // set the spinner
        spEnd.setAdapter(adapterTime);
        spEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get data from spinner
                endTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // set the spinner
        spCourtNo.setAdapter(adapterCourtNo);
        spCourtNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get data from spinner
                courtNo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //get user selected date
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog  = new DatePickerDialog(UpdateBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        date = (day+"-"+month+"-"+year);
                        editTextDate.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        confirmBookingBtn = findViewById(R.id.buttonConfirmBookingUpdate);


        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //readData(new FirebaseCallBack() {
                    //@Override
                    //public void onCallback(final String s, final String e,final String n,final String a,final String d,final String na,final String m,final String ui) {
                       //startTime1=s;
                        //endTime1=e;
                        //noCourt= n;
                       // address = a;
                       // date1 = d;
                       // name = na;
                        //method = m;
                        //userID = ui;

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SportCentre");
                            final DatabaseReference sReference= reference.child("Lavana Sport Centre");
                            final DatabaseReference dRef = sReference.child("16-11-2020");
                            final DatabaseReference ddRef = sReference.child("16-11-2020");



                        startTime1 = "1000";
                        endTime1 = "1200";
                        noCourt = "2";

                        sTime1 = Count(startTime1);
                        eTime1 = Count(endTime1);
                        nc1 = Count(noCourt);

                        //Read and listen for changes to the entire contents of a path.
                        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                    //check whether is current time
                                    if (testTime < sTime1) {
                                        testTime = testTime + 100;
                                    } else {
                                        //check whether the court is enough
                                        if (mCount <= ((eTime1 - sTime1) / 100) * nc1) {
                                            //check whether booking is enough at that hour
                                            if (sTime1 < eTime1) {
                                                int countCourt1 = 1, noCourt1 = 1;
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    BookingStatus bs = dataSnapshot2.getValue(BookingStatus.class);
                                                    if (countCourt1 <= nc1 && noCourt1 <= 4) {
                                                        //check whether the court have been booked by the user that need to update
                                                        if (bs.getStatus().equals("Yes") && bs.getName().equals(name)) {
                                                            //change status to no
                                                            String bookingStatus = "No";
                                                            String cNumber = "Court" + noCourt1;
                                                            String bookName = "";
                                                            BookingStatus bs1 = new BookingStatus(cNumber, bookName, bookingStatus);

                                                            String st = String.valueOf(sTime1);
                                                            tReference = dRef.child(st);
                                                            tReference.child(cNumber).setValue(bs1);
                                                            mCount++;
                                                            countCourt1++;
                                                        }
                                                        noCourt1++;
                                                    }
                                                }
                                            }
                                            sTime1 = sTime1 + 100;
                                            testTime = testTime + 100;
                                        }
                                    }
                                }
                                ddRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dsnapshot) {

                                        sTime = Count(startTime);
                                        eTime = Count(endTime);
                                        nc = Count(noCourt);

                                        for (DataSnapshot snapshot1 : dsnapshot.getChildren()) {
                                            if (calTime < sTime)
                                            {
                                                calTime = calTime +100;
                                            }
                                            else{
                                                if (sTime < eTime) {
                                                    int countCourt = 1, noCourt = 1;

                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                                                        BookingStatus bs = snapshot2.getValue(BookingStatus.class);

                                                        if (countCourt <= nc && noCourt <= 4) {
                                                            if (bs.getStatus().equals("No")) {
                                                                countCourt++;
                                                                k++;
                                                            }
                                                            noCourt++;
                                                        }

                                                    }

                                                }
                                                sTime = sTime + 100;
                                                calTime = calTime+100;
                                            }

                                        }

                                        sTime = Count(startTime);
                                        eTime = Count(endTime);
                                        nc = Count(noCourt);

                                        countTotalPrice = (((eTime-sTime)/100)*nc)*10.00;
                                        totalPrice = String.valueOf(countTotalPrice);

                                        hour =(eTime-sTime)/100;
                                        totalHC = nc*hour;
                                        testTime=1000;
                                        mCount=0;
                                        k=0;

                                        //check whether time is available
                                        if (totalHC != k) {
                                            timeError.setText("Time is not available");
                                        }
                                        else{
                                            for (DataSnapshot datasnapshot1 : dsnapshot.getChildren()) {
                                                //check whether the court refer to the booking start time
                                                if (testTime < sTime) {
                                                    testTime = testTime + 100;
                                                } else {
                                                    if(mCount<=k) {
                                                        // check booking is enough for that hour
                                                        if (sTime < eTime) {
                                                            int countCourt1 = 1, noCourt1 = 1;

                                                            for (DataSnapshot datasnapshot2 : datasnapshot1.getChildren()) {

                                                                BookingStatus bs = datasnapshot2.getValue(BookingStatus.class);

                                                                //check whether booking is enough
                                                                if (countCourt1 <= nc && noCourt1 <= 4) {
                                                                    //check whether the court have been booked
                                                                    if (bs.getStatus().equals("No")) {
                                                                        String bookingStatus = "Yes";
                                                                        String cNumber = "Court" + noCourt1;


                                                                        BookingStatus bs1 = new BookingStatus(cNumber, name, bookingStatus);

                                                                        String st = String.valueOf(sTime);
                                                                        tReference = dRef.child(st);
                                                                        tReference.child(cNumber).setValue(bs1);
                                                                        mCount++;
                                                                        countCourt1++;

                                                                    }
                                                                    noCourt1++;
                                                                }

                                                            }

                                                        }
                                                        sTime = sTime + 100;
                                                        testTime = testTime + 100;
                                                    }
                                                    ddRef.removeEventListener(this);
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        validateDate();
                        validateCourt(courtNo);
                        validateTime(startTime,endTime);



                        if(validateDate() && validateCourt(courtNo) && validateTime(startTime,endTime)){

                            Payment pay = new Payment(date,endTime,id,address,name,noCourt,method,startTime,totalPrice,userID);
                            paymentReference1.setValue(pay);

                            String message = "Your Have successfully Update your the booking\n"+"Booking Date:"+date+"\n"+"Booking time:"+startTime+"to"+endTime;

                            sendMessage(address,userName,message);

                            Intent intent1 = new Intent(UpdateBookingActivity.this, DisplayBookingDetailsActivity.class);
                            intent1.putExtra("bookingID",id);
                            intent1.putExtra("userName",userName);
                            startActivity(intent1);

                        }

                    //}
                //});
            }


       });


    }

    private void readData(final FirebaseCallBack firebaseCallBack){

        ref = FirebaseDatabase.getInstance().getReference().child("Payment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()) {
                    Payment pay = snapshot1.getValue(Payment.class);
                    if(pay.getId().equals(id)){
                        startTime1 = pay.getStartTime();
                        endTime1 = pay.getEndTime();
                        noCourt = pay.getNoCourt();
                        address = pay.getLocation();
                        date1 = pay.getDate();
                        name = pay.getName();
                        method = pay.getPaymentStatus();
                        userID = pay.getUserID();

                    }
                }
                firebaseCallBack.onCallback(startTime1,endTime1,noCourt,address,date,name,method,userID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readData(final FirebaseCallBack1 firebaseCallBack1){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SportCentre");
        DatabaseReference sReference= reference.child(address);
        DatabaseReference dReference = sReference.child(date);
        dReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (calTime < sTime)
                    {
                        calTime = calTime +100;
                    }
                    else{
                        if (sTime < eTime) {
                            int countCourt = 1, noCourt = 1;

                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {

                                BookingStatus bs = snapshot2.getValue(BookingStatus.class);

                                if (countCourt <= nc && noCourt <= 4) {
                                    if (bs.getStatus().equals("No")) {
                                        countCourt++;
                                        k++;
                                    }
                                    noCourt++;
                                }

                            }

                        }
                        sTime = sTime + 100;
                        calTime = calTime+100;
                    }

                }
                firebaseCallBack1.onCallback(k);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }


    interface FirebaseCallBack{
        void onCallback(String s, String e,String n, String a,String d,String na,String m,String ui);
    }

    private interface FirebaseCallBack1{
        void onCallback(int i);
    }


    //Validate date
    private boolean validateDate(){

        String bpDate = editTextDate.getText().toString();

        if(bpDate.isEmpty()){
            dateError.setText("Field cannot be empty");
            return false;
        }
        else{
            dateError.setText("");
            return true;
        }
    }

    //validate time
    private boolean validateTime(String startTime,String endTime) {

        int sTime, eTime;

        try {
            sTime = Integer.parseInt(startTime);
            eTime = Integer.parseInt(endTime);

            if (sTime > eTime) {
                timeError.setText("Time error");
                return false;

            }
            else {
                timeError.setText("");
                return true;
            }

        } catch (NumberFormatException ex) {

            timeError.setText("Please select a time");
            return false;
        }
    }

    //validate number of court
    private boolean validateCourt(String courtNo){

        if(courtNo.equals("Number of Courts") ){
            courtNoError.setText("Please Select Number of Court");
            return false;
        }
        else {
            courtNoError.setText("");
            return true;
        }
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

    //send message while payment done
    private void sendMessage(final String sender, final String receiver, final String message){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat");
        String id = reference.push().getKey();
        String status = "true";
        Chat chat = new Chat(sender,receiver,message,status);

        reference.child(id).setValue(chat);

        //Read and listen for changes to the entire contents of a path.
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);

                    //get the latest message
                    if(chat.getSender().equals(sender) && chat.getReceiver().equals(receiver) && chat.getStatus().equals("true")){
                        String id = snapshot1.getKey();
                        reference.child(id).child("status").setValue("false");

                        //set notification
                        mNotificationHelper = new NotificationHelper(UpdateBookingActivity.this);
                        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(receiver,message,sender);
                        mNotificationHelper.getManager().notify(1,nb.build());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}