package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordVerficationActivity extends AppCompatActivity {

    EditText edt_otp;
    RelativeLayout btn_otp;
    String code, otp, phone;

    TextView txtSendAgain;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_verfication);


        try {
            //forgot password verification
            code = getIntent().getStringExtra("code");
            edt_otp = findViewById(R.id.edt_forgot_otp);
            btn_otp = findViewById(R.id.btn_verify_forgot_otp);
            txtSendAgain = findViewById(R.id.txt_forgot_sendAgain);

            phone = getIntent().getStringExtra("phone");


            txtSendAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            // login user without sending otp
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            Toast.makeText(ForgotPasswordVerficationActivity.this, "verification failed", Toast.LENGTH_SHORT).show();

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(ForgotPasswordVerficationActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();

                            } else if (e instanceof FirebaseTooManyRequestsException) {

                                Toast.makeText(ForgotPasswordVerficationActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            Toast.makeText(ForgotPasswordVerficationActivity.this, "code sent", Toast.LENGTH_SHORT).show();
                        }
                    };
                    if (!phone.isEmpty()) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone,
                                60,
                                TimeUnit.SECONDS,
                                ForgotPasswordVerficationActivity.this,
                                callback);




                    }
                }
            });





            btn_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        otp = edt_otp.getText().toString().trim();
                        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(code, otp);
                        FirebaseAuth.getInstance().signInWithCredential(credentials).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent takeToLogin = new Intent(getApplicationContext(),Login.class);
                                startActivity(takeToLogin);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPasswordVerficationActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch(Exception e){
                        Toast.makeText(Vendor.getAppContext(), "On Send OTP: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create View: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
