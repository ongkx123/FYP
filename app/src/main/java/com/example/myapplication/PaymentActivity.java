package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PaymentActivity extends AppCompatActivity {

    TextView totalPrice,bookName,bookDate,bookTime,bookNoCourt,paymentError;
    String bName,bDate,bStartTime,bEndTime,bNoCourt,bLocation;
    Spinner paymentMethod;
    Button buttonMakePayment;
    DatabaseReference ref;
    private static DecimalFormat df2 = new DecimalFormat("0.00");

    double price = 10.00;
    double tPrice;

    int st,et;
    int counter=0;
    String dPrice;
    String method;
    String bID;
    String message;
    String userName;
    DatabaseReference reference;

    private int PAYPAL_REQ_CODE=12;
    private PayPalConfiguration paypalConfig = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(BookingPaypalClientConfigClass.PAYPAL_CLIENT_ID);

    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //get data from previous activity
        if(getIntent().hasExtra("book name")&&getIntent().hasExtra("book date")&&getIntent().hasExtra("start time")&&getIntent().hasExtra("end time")&&getIntent().hasExtra("court number") && getIntent().hasExtra("userName")){
            bName = getIntent().getStringExtra("book name");
            bDate = getIntent().getStringExtra("book date");
            bStartTime = getIntent().getStringExtra("start time");
            bEndTime = getIntent().getStringExtra("end time");
            bNoCourt = getIntent().getStringExtra("court number");
            bLocation = getIntent().getStringExtra("location");
            userName = getIntent().getStringExtra("userName");
        }

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,paypalConfig);
        startService(intent);

        totalPrice = (TextView)findViewById(R.id.textViewPrice);
        bookName = (TextView)findViewById(R.id.textViewName);
        bookDate = (TextView)findViewById(R.id.textViewDate);
        bookTime = (TextView)findViewById(R.id.textViewTime);
        bookNoCourt = (TextView)findViewById(R.id.textViewNoCourt);
        paymentError = (TextView)findViewById(R.id.textViewPaymentError);
        buttonMakePayment = (Button)findViewById(R.id.buttonMakePayment);
        paymentMethod = (Spinner)findViewById(R.id.spinnerPaymentMethod);

        ref = FirebaseDatabase.getInstance().getReference().child("Payment");

        //spinner setting
        ArrayAdapter<CharSequence> adapterPaymentMethod= ArrayAdapter.createFromResource(this,R.array.paymentMethod,R.layout.color_spinner_layout);
        adapterPaymentMethod.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        paymentMethod.setAdapter(adapterPaymentMethod);
        paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //get data from spinner
                method = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        st = getCount(bStartTime);
        et = getCount(bEndTime);

        //calculate total hour
        while(st<et){
           counter++;
           st = st+100;
        }


        //calculate price
        tPrice = price * getCount(bNoCourt) * counter;
        dPrice = df2.format(tPrice);
        totalPrice.setText("RM"+dPrice);
        bookName.setText(bName);
        bookDate.setText(bDate);
        bookTime.setText(bStartTime + " - " +bEndTime);
        bookNoCourt.setText(bNoCourt);

        //select payment method
        buttonMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check payment method
                if(method.equals("Payment Method")) {
                    paymentError.setText("Please select a payment Method");
                }
                else {
                    bID = ref.push().getKey();
                    Payment p = new Payment(bDate, bEndTime,bID,bLocation, bName, bNoCourt, method, bStartTime, dPrice,userName);
                    ref.child(bID).setValue(p);

                    if(method.equals("Paypal")){
                        paypalPaymentMethod();
                    }
                    else if(method.equals("Pay at Counter")){

                        message = "Your Have successfully make the booking\n"+"Booking Date:"+bDate+"\n"+"Booking time:"+bStartTime+"to"+bEndTime;

                        sendMessage(bLocation,userName,message);

                        checkMessage(bLocation,userName,message);

                        Intent intent1 = new Intent(PaymentActivity.this, DisplayBookingDetailsActivity.class);
                        intent1.putExtra("bookingID",bID);
                        intent1.putExtra("userName",userName);
                        startActivity(intent1);

                    }
                }
            }
        });


    }



    public  int getCount(String s){
        int num;

        try {
            num = Integer.parseInt(s);
            return num;
        }catch (NumberFormatException ex){
            return 0;
        }
    }

    //intent to paypal
    private void paypalPaymentMethod(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(tPrice),"MYR","Booking Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this,    com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,paypalConfig);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT,payment);

        startActivityForResult(intent,PAYPAL_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQ_CODE){
            Toast.makeText(this, "Payment made sucessfully", Toast.LENGTH_SHORT).show();
            message = "Your Have successfully make the booking\n"+"Booking Date:"+bDate+"\n"+"Booking time:"+bStartTime+"to"+bEndTime;
            sendMessage(bLocation,userName,message);
            Intent intent1 = new Intent(PaymentActivity.this, DisplayBookingDetailsActivity.class);
            intent1.putExtra("bookingID",bID);
            intent1.putExtra("userName",userName);
            startActivity(intent1);
        }
        else{
            Toast.makeText(this, "Payment made Unsucessfully", Toast.LENGTH_SHORT).show();
        }
    }

    protected  void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    //send message while payment done
    private void sendMessage(final String sender, final String receiver, final String message){
        reference = FirebaseDatabase.getInstance().getReference().child("Chat");
        String id = reference.push().getKey();
        String status = "true";
        Chat chat = new Chat(sender,receiver,message,status);

        reference.child(id).setValue(chat);



    }

    private void checkMessage(final String sender, final String receiver, final String message){
        reference = FirebaseDatabase.getInstance().getReference().child("Chat");
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
                        mNotificationHelper = new NotificationHelper(PaymentActivity.this);
                        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(sender,message,userName);
                        mNotificationHelper.getManager().notify(1,nb.build());
                    }
                    else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}