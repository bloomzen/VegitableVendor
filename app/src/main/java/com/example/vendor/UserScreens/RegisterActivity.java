package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.HotelList;
import com.example.vendor.HotelNameContsnats;
import com.example.vendor.Network_Connectivity.ConnectingReceview;
import com.example.vendor.Constants;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class RegisterActivity extends AppCompatActivity {

    ImageButton btn_back, btn_hotelName;
    EditText edt_reg_fullName, edt_reg_phone, edt_reg_password, edt_reg_confirmPassword, edt_hotelName;

  RelativeLayout btn_register;
    TextView txt_signIn, txt_HotelName;


    String selected;
    ProgressDialog mProgressDialog;

    DatabaseReference mRef, db;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;
    public List<String> HotelNames ;

    String[] array;



    //register user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            db =FirebaseDatabase.getInstance().getReference().child("HotelName");


            btn_back = findViewById(R.id.btn_register_back);
            edt_reg_fullName = findViewById(R.id.edt_register_FullName);
            edt_reg_confirmPassword = findViewById(R.id.edt_register_ConfirmPassword);
            edt_reg_phone = findViewById(R.id.edt_register_Phone);

            edt_reg_password = findViewById(R.id.edt_register_Password);
            edt_hotelName = findViewById(R.id.edt_hotelName);
            btn_register = findViewById(R.id.btn_register);
            txt_signIn = findViewById(R.id.txt_signIn_register);



            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Please Wait");
            mProgressDialog.setCanceledOnTouchOutside(false);





            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputData();
                }
            });

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }







    }

    private String fullName, phone, emial, password, confirmPassword, hotelName;

    private void inputData() {

        try {
            fullName = edt_reg_fullName.getText().toString().trim();
            phone = edt_reg_phone.getText().toString().trim();

            password = edt_reg_password.getText().toString().trim();
            confirmPassword = edt_reg_confirmPassword.getText().toString();
            hotelName = edt_hotelName.getText().toString().trim();

            if(fullName.isEmpty() && phone.isEmpty()  && password.isEmpty() && confirmPassword.isEmpty()){
                Toast.makeText(this, "empty fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fullName.isEmpty()) {
                Toast.makeText(this, "Please enter full name....", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty() && !TextUtils.isDigitsOnly(phone) && phone.length() == 10) {
                Toast.makeText(this, "Please enter correct phone number....", Toast.LENGTH_SHORT).show();
                return;
            }


            if (password.length() < 6) {
                Toast.makeText(this, "passwords should have minimum 6 characters", Toast.LENGTH_SHORT).show();
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password and confirm Password doesn't matches", Toast.LENGTH_SHORT).show();
                return;
            }

            checkAccount();
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "input Data"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    private void checkAccount() {

        try {
            mProgressDialog.setMessage("Checking the Credentials...");
            mProgressDialog.show();
            mRef = FirebaseDatabase.getInstance().getReference().child("User");

            if (ConnectingReceview.checkConnection(getApplicationContext())) {
                // Its Available...


                mRef.orderByChild("UserPhone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            mProgressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Phone number is already present", Toast.LENGTH_SHORT).show();
                        } else {

                            mProgressDialog.hide();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone,
                                    60,
                                    TimeUnit.SECONDS,
                                    RegisterActivity.this,
                                    callback);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            } else {
                // Not Available...
                mProgressDialog.hide();
                Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
            }

            callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // login user without sending otp
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    Toast.makeText(RegisterActivity.this, "verification failed", Toast.LENGTH_SHORT).show();

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(RegisterActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {

                        Toast.makeText(RegisterActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Toast.makeText(RegisterActivity.this, "code sent", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(getApplicationContext(), UserAuthenticationActivity.class);
                    registerIntent.putExtra("name", fullName);
                    registerIntent.putExtra("hotelName",hotelName);
                    registerIntent.putExtra("phone", phone);

                    registerIntent.putExtra("password", password);
                    registerIntent.putExtra("code", s);
                    registerIntent.putExtra("userType","guest");


                    startActivity(registerIntent);
                    finish();

                }
            };
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "check Account: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProgressDialog.dismiss();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }
}
