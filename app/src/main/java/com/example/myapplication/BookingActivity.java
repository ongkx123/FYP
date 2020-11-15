package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

    public class BookingActivity extends AppCompatActivity {

        ImageView imageViewSportCentre;
        TextView textViewName;
        DatePickerDialog datePickerDialog;
        Button confirmBookingBtn;
        EditText personName, editTextDate;
        Spinner spStart,spEnd,spCourtNo;
        String startTime,endTime,courtNo;
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        TextView nameError,dateError,timeError,courtNoError;
        String date;
        TextView textView;
        int k = 0;
        String status;
        int eTime,sTime ,courtNumber;
        String court;
        int m=0;
        String name,url;
        DatabaseReference ref,ref1,ref2,ref3;
        int calTime=1000;
        int getCal;
        String bookDate;
        int hour,totalHC;
        int testTime=1000;
        DatabaseReference ref4,ref5,ref6,ref7;
String userName;
        int trueWrong;




        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //get data from the previous activity
        if(getIntent().hasExtra("name")&&getIntent().hasExtra("url")&&getIntent().hasExtra("userName")){
            name = getIntent().getStringExtra("name");
            url = getIntent().getStringExtra("url");
            userName = getIntent().getStringExtra("userName");
        }

        nameError = (TextView)findViewById(R.id.textViewNameError);
        dateError = (TextView)findViewById(R.id.textViewDateError);
        timeError = (TextView)findViewById(R.id.textViewTimeError);
        courtNoError = (TextView)findViewById(R.id.textViewCourtError);

        imageViewSportCentre = (ImageView)findViewById(R.id.imageViewSportCentre);
        textViewName = (TextView)findViewById(R.id.textViewSportCentreName);

        Picasso.get().load(url).into(imageViewSportCentre);
        textViewName.setText(name + " Booking");


        personName = findViewById(R.id.editTextTextPersonName);
        editTextDate = (EditText) findViewById(R.id.editTextDate);

        // set calender
        Calendar cal  = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        spStart = (Spinner)findViewById(R.id.spinnerStartTime);
        spEnd = (Spinner)findViewById(R.id.spinnerEndTime);
        spCourtNo = (Spinner)findViewById(R.id.spinnerCourtNo);

        //set the spinner
        ArrayAdapter<CharSequence> adapterTime = ArrayAdapter.createFromResource(this,R.array.time, R.layout.color_spinner_layout);
        adapterTime.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        ArrayAdapter<CharSequence> adapterCourtNo = ArrayAdapter.createFromResource(this,R.array.courtNo, R.layout.color_spinner_layout);
        adapterCourtNo.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spStart.setAdapter(adapterTime);

        //get the data on the spinner
        spStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spEnd.setAdapter(adapterTime);
        spEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCourtNo.setAdapter(adapterCourtNo);
        spCourtNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courtNo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        confirmBookingBtn = (Button)findViewById(R.id.buttonConfirmBooking);


        // get the user selected date
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog  = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get database reference
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Booking");

                final String bookName = personName.getText().toString();
                bookDate = editTextDate.getText().toString();


                //check validation
                validateName();
                validateDate();
                validateCourt(courtNo);
                validateTime(startTime,endTime);

                sTime = getStartTime(startTime);
                eTime = getEndTime(endTime);
                courtNumber = getCourtNo(courtNo);

                hour =(eTime-sTime)/100;
                totalHC = courtNumber*hour;

                readData1(new FirebaseCallBack1() {
                    @Override
                    public void onCallback1(int i) {
                        trueWrong = i;
                        //check whether the sport center operate on the selected date
                        if(trueWrong==1){
                            dateError.setText("The sport centre do not open on today");
                        }
                        else if(trueWrong==0){

                            dateError.setText("");

                if(validateName() || validateDate() || validateCourt(courtNo) || validateTime(startTime,endTime)) {

                    readData(new FirebaseCallBack() {
                        @Override
                        public void onCallback(int i) {
                            getCal = i;

                            // check enough court on that time
                            if (totalHC != getCal) {
                                timeError.setText("Time is not available");
                            }
                            else{

                                //refer to database reference
                                ref4 = FirebaseDatabase.getInstance().getReference().child("SportCentre");
                                ref5 = ref4.child(name);
                                ref6 = ref5.child(bookDate);

                                sTime = getStartTime(startTime);
                                eTime = getEndTime(endTime);


                                ref6.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                        //Read and listen for changes to the entire contents of a path.
                                        for (DataSnapshot datasnapshot1 : datasnapshot.getChildren()) {

                                            if (testTime < sTime) {
                                                testTime = testTime + 100;
                                            } else {
                                                if(m<=getCal) {
                                                    if (sTime < eTime) {
                                                        int countCourt1 = 1, noCourt1 = 1;

                                                        for (DataSnapshot datasnapshot2 : datasnapshot1.getChildren()) {

                                                            //pass data ninto the booking status class
                                                            BookingStatus bs = datasnapshot2.getValue(BookingStatus.class);

                                                            //check  whether have booked enough court
                                                            if (countCourt1 <= courtNumber && noCourt1 <= 4) {
                                                                //check whether the court have been booked
                                                                if (bs.getStatus().equals("No")) {
                                                                    String bookingStatus = "Yes";
                                                                    String cNumber = "Court" + noCourt1;


                                                                    BookingStatus bs1 = new BookingStatus(cNumber, bookName, bookingStatus);

                                                                    String st = String.valueOf(sTime);
                                                                    ref7 = ref6.child(st);
                                                                    ref7.child(cNumber).setValue(bs1);
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


                                //Intent to the payment activity
                                Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                                intent.putExtra("location", name);
                                intent.putExtra("book name", personName.getText().toString());
                                intent.putExtra("book date", editTextDate.getText().toString());
                                intent.putExtra("start time", startTime);
                                intent.putExtra("end time", endTime);
                                intent.putExtra("court number", courtNo);
                                intent.putExtra("userName",userName);
                                startActivity(intent);
                            }
                        }
                    });




                }
            }
        }
        });

            }

        });
    }

    //check whether have enough court
        private void readData(final FirebaseCallBack firebaseCallBack){
            ref = FirebaseDatabase.getInstance().getReference().child("SportCentre");
            ref1 = ref.child(name);
            ref2 = ref1.child(bookDate);
            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
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

                                    if (countCourt <= courtNumber && noCourt <= 4) {
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
                    firebaseCallBack.onCallback(k);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    return;
                }
            });


        }

        private interface FirebaseCallBack{
            void onCallback(int i);
        }

        //check whether the sport center operate on that day
        private void readData1(final FirebaseCallBack1 firebaseCallBack1){
            final String bpDate = editTextDate.getText().toString();
            ref = FirebaseDatabase.getInstance().getReference().child("SportCentre");
            ref1 = ref.child(name);
            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //operate on that day
                    if(snapshot.child(bpDate).exists()){
                        trueWrong = 0;
                    }
                    //not operate on that day
                    else{
                        trueWrong = 1;
                    }

                    firebaseCallBack1.onCallback1(trueWrong);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    return;
                }
            });


        }

        private interface FirebaseCallBack1{
            void onCallback1(int i);
        }

        //Validate name
        private boolean validateName(){

            String bpName = personName.getText().toString();
            //Name field is empty
            if(bpName.isEmpty()){
                nameError.setText("Field cannot be empty");
                return false;
            }
            else{
                nameError.setText("");
                return true;
            }
        }

        private boolean validateDate(){

            String bpDate = editTextDate.getText().toString();
            //Date field is empty
            if(bpDate.isEmpty()){
                dateError.setText("Field cannot be empty");
                return false;
            }
            else{
                dateError.setText("");
                return true;
            }
        }

        private boolean validateTime(String startTime,String endTime) {

        int sTime, eTime;

            try {
                sTime = Integer.parseInt(startTime);
                eTime = Integer.parseInt(endTime);
                //start time later than end time
                if (sTime > eTime) {
                    timeError.setText("Time error");
                    return false;

                }
                else {
                    timeError.setText("");
                    return true;
                }

            } catch (NumberFormatException ex) {

                //not select any time
                timeError.setText("Please select a time");
                return false;
            }
        }

        private boolean validateCourt(String courtNo){

            //not select number of court
            if(courtNo.equals("Number of Courts") ){
                courtNoError.setText("Please Select Number of Court");
                return false;
            }
            else {
                courtNoError.setText("");
                return true;
            }
        }

        public int getStartTime(String startTime){

            int sTime;

            try {
                sTime = Integer.parseInt(startTime);
                return sTime;
            }catch (NumberFormatException ex){
                return 0;
            }
        }

        public int getEndTime(String endTime){

            int eTime;

            try {
                eTime = Integer.parseInt(endTime);
                return eTime;
            }catch (NumberFormatException ex){
                return 0;
            }
        }
        public int getCourtNo(String courtNumber){

            int CourtNumber;

            try {
                CourtNumber = Integer.parseInt(courtNumber);
                return CourtNumber;
            }catch (NumberFormatException ex){
                return 0;
            }
        }



    }
