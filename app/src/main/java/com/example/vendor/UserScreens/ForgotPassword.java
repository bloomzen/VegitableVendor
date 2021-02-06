package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    EditText edt_email_phone, edt_password, edt_confirmPassword, edt_forgot_otp;
    RelativeLayout btn_forgot_verfiy;

    String email_phone, password, confirmPassword, btn_text;
    DatabaseReference mRef, mAdminRef;
    String userId, edt_otp;

    ProgressDialog mProgress;
    int admin = 0;
    String token;

    String email_otp;



    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        try {
            edt_email_phone = findViewById(R.id.edt_forgot_email);

            btn_forgot_verfiy = findViewById(R.id.btn_verify_forgot);



            admin = getIntent().getIntExtra("admin", 0);

            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Please Wait...");
            mProgress.setCanceledOnTouchOutside(false);

            mAuth = FirebaseAuth.getInstance();
            mRef = FirebaseDatabase.getInstance().getReference().child("User");
            mAdminRef = FirebaseDatabase.getInstance().getReference().child("Admin");

            btn_forgot_verfiy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    senOTP();

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create View: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }






    }



    //send otp
    private void senOTP() {

        try {
            mProgress.setMessage("Sending otp...");

            mProgress.show();
            if (admin == 0) {
                email_phone = edt_email_phone.getText().toString().trim();
                if (TextUtils.isDigitsOnly(email_phone) && email_phone.length() >= 10) {
                    mRef.orderByChild("UserPhone").equalTo(email_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    mProgress.dismiss();
                                    userId = child.getKey();
                                    email_phone = edt_email_phone.getText().toString().trim();

                                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + email_phone,
                                            60,
                                            TimeUnit.SECONDS,
                                            ForgotPassword.this,
                                            callback);



                                }
                            } else {
                                //its new users
                                mProgress.hide();
                                Toast.makeText(Vendor.getAppContext(), "Not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    mProgress.hide();
                    Toast.makeText(this, "please input right credential", Toast.LENGTH_SHORT).show();
                }

            }else  if(admin == 1){

                //used when admin forgot password
                email_phone = edt_email_phone.getText().toString().trim();
                if (TextUtils.isDigitsOnly(email_phone) && email_phone.length() >= 10) {
                    mAdminRef.orderByChild("UserPhone").equalTo(email_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    mProgress.dismiss();
                                    userId = child.getKey();
                                    email_phone = edt_email_phone.getText().toString().trim();

                                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + email_phone,
                                            60,
                                            TimeUnit.SECONDS,
                                            ForgotPassword.this,
                                            callback);



                                }
                            } else {
                                //its new users
                                mProgress.hide();
                                Toast.makeText(getApplicationContext(), "Not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    mProgress.hide();
                    Toast.makeText(this, "please input right credential", Toast.LENGTH_SHORT).show();
                }
            }


            callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // login user without sending otp
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    Toast.makeText(getApplicationContext(), "verification failed", Toast.LENGTH_SHORT).show();

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(getApplicationContext(), "Invalid phone number.", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {

                        Toast.makeText(getApplicationContext(), "Quota exceeded.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "code sent", Toast.LENGTH_SHORT).show();



                    Intent intent = new Intent(getApplicationContext(),ForgotPasswordVerficationActivity.class);
                    intent.putExtra("code",s);
                    intent.putExtra("phone",email_phone);
                    startActivity(intent);
                    finish();


                }
            };
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Code Sent: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }










}
