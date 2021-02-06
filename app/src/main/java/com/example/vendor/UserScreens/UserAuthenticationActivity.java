package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class UserAuthenticationActivity extends AppCompatActivity {

    private String fullName, phone, userType, password, hotelName, code;
    EditText edt_otp;
   RelativeLayout btn_register;

    String otp;
    String phoneUid, emailUid;
    FirebaseAuth mAuth;
    DatabaseReference mRef;

    ProgressDialog mProgress;
    TextView txt_sendAgain;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;
    
    



    //authenticate the registered user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);


        try {
            mAuth = FirebaseAuth.getInstance();
            mRef = FirebaseDatabase.getInstance().getReference().child("User");


            fullName = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");

            password = getIntent().getStringExtra("password");
            hotelName = getIntent().getStringExtra("hotelName");
            userType = getIntent().getStringExtra("userType");


            mProgress = new ProgressDialog(this);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setTitle("Please wait");


            edt_otp = findViewById(R.id.codeText);
            btn_register = findViewById(R.id.btn_register_User);
            txt_sendAgain = findViewById(R.id.txt_sendAgain);

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAccount();
                }
            });

            txt_sendAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            // login user without sending otp
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            Toast.makeText(UserAuthenticationActivity.this, "verification failed", Toast.LENGTH_SHORT).show();

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(UserAuthenticationActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();

                            } else if (e instanceof FirebaseTooManyRequestsException) {

                                Toast.makeText(UserAuthenticationActivity.this, "Quota exceeded.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            Toast.makeText(UserAuthenticationActivity.this, "code sent", Toast.LENGTH_SHORT).show();



                        }
                    };

                    if (!phone.isEmpty()) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone,
                                60,
                                TimeUnit.SECONDS,
                                UserAuthenticationActivity.this,
                                callback);




                    }


                }


            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }




    private void createAccount() {

        try {
            mProgress.setMessage("Creating Account....");
            mProgress.show();

            otp = getIntent().getStringExtra("code");

            code = edt_otp.getText().toString().trim();

            PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(otp, code);

            mAuth.signInWithCredential(credentials).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser mPhoneUser = authResult.getUser();
                    phoneUid = mPhoneUser.getUid();


                    FirebaseUser currentuser = mAuth.getCurrentUser();
                    emailUid = currentuser.getUid();
                    mRef = mRef.child(emailUid);
                    HashMap<String, String> users = new HashMap<>();
                    users.put("UserName", fullName);

                    users.put("UserPhone", phone);
                    users.put("UserHotelName", hotelName);
                    users.put("UserPassword", password);
                    users.put("image","");
                    users.put("UserType",userType);


                    mRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd hh:mm aa");
                                String currentDateTime = sdf.format(new Date());
                                mRef.child("registeredTime").setValue(currentDateTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            mProgress.dismiss();
                                            Toast.makeText(getApplicationContext(), "successfully created account", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                            SharedPreferences sh = getSharedPreferences("MyLogin", MODE_PRIVATE);

                                            SharedPreferences.Editor myEdit = sh.edit();
                                            myEdit.putString("userId", mAuth.getUid());
                                            myEdit.putString("userType",userType);

                                            myEdit.apply();
                                            startActivity(intent);
                                        } else {
                                            mProgress.hide();
                                            Toast.makeText(UserAuthenticationActivity.this, "unsuccessful to create account", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });


                            } else {
                                mProgress.hide();
                                Toast.makeText(UserAuthenticationActivity.this, "error in storing data. trying again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.hide();
                    Toast.makeText(UserAuthenticationActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Account Crated: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }


//        mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    FirebaseUser mPhoneUser = task.getResult().getUser();
//                    phoneUid = mPhoneUser.getUid();
//
//
//                    FirebaseUser currentuser = mAuth.getCurrentUser();
//                    emailUid = currentuser.getUid();
//                    mRef = mRef.child(emailUid);
//                    HashMap<String, String> users = new HashMap<>();
//                    users.put("Name", fullName);
//                    users.put("email", emial);
//                    users.put("phone", phone);
//                    users.put("HotelName", hotelName);
//                    users.put("password",password);
//
//
//
//                    mRef.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task1) {
//                            if (task1.isSuccessful()) {
//
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd GGG hh:mm aa");
//                                String currentDateTime = sdf.format(new Date());
//                                mRef.child("registeredTime").setValue(currentDateTime).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if(task.isSuccessful()){
//                                            mProgress.dismiss();
//                                            Toast.makeText(getApplicationContext(), "successfully created account", Toast.LENGTH_LONG).show();
//                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                            startActivity(intent);
//                                        }else {
//                                            mProgress.hide();
//                                            Toast.makeText(UserAuthenticationActivity.this, "unsuccessful to create account", Toast.LENGTH_SHORT).show();
//                                        }
//
//
//
//
//                                    }
//                                });
//
//
//                            }else {
//                                mProgress.hide();
//                                Toast.makeText(UserAuthenticationActivity.this, "error in storing data. trying again later", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }else {
//                    mProgress.hide();
//                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }
}
