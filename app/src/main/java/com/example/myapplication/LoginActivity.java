package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginPhoneNum, loginPassword;
    private Button loginButton;
    private TextView sellerLink, notSellerLink, signUpLink;
    private String parentNode = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        loginPhoneNum = (EditText) findViewById(R.id.login_phone);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        sellerLink = (TextView) findViewById(R.id.seller_panel);
        notSellerLink = (TextView) findViewById(R.id.not_seller_panel);
        signUpLink = (TextView) findViewById(R.id.sign_up_btn);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                userLogin();
            }
        });

        sellerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Seller Login");
                sellerLink.setVisibility(View.INVISIBLE);
                notSellerLink.setVisibility(View.VISIBLE);
                parentNode = "Sellers";

                signUpLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, LocationSearchBookingActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        notSellerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                sellerLink.setVisibility(View.VISIBLE);
                notSellerLink.setVisibility(View.INVISIBLE);
                parentNode = "Users";

                signUpLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, LocationSearchBookingActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    private void userLogin()
    {
        String phone = loginPhoneNum.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            loginPhoneNum.setError("Please enter the phone number");
        }
        else if (TextUtils.isEmpty(password))
        {
            loginPassword.setError("Please enter the password");
        }
        else
        {
            userLoginSuccess(phone, password);
        }

    }

    private void userLoginSuccess(final String phone, final String password)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();  //Refer to firebase

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentNode).child(phone).exists())  //Check whether exist
                {
                    //pass value to Users class
                    Users userData = dataSnapshot.child(parentNode).child(phone).getValue(Users.class);

                    //Check whether phone and password by user input is equal to database
                    if(userData.getPhone().equals(phone))  //If user input equals the phone data in firebase
                    {
                        if(userData.getPassword().equals(password))  //If user input equals the password data in firebase
                        {
                            if (parentNode.equals("Sellers"))   //If the parentNode in firebase equals Sellers
                            {
                                Toast.makeText(LoginActivity.this,"Seller Login successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, LocationSearchBookingActivity.class);
                                startActivity(intent);
                            }
                            else if (parentNode.equals("Users"))  //If the parentNode in firebase equals Users
                            {
                                String userName = userData.getName();
                                Toast.makeText(LoginActivity.this,"Login successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, LocationSearchBookingActivity.class);
                                intent.putExtra("userName",userName);

                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"This account does not exist, pls try again", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this,"This account does not exist, pls try again", Toast.LENGTH_SHORT).show();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}