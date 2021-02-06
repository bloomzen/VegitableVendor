package com.example.vendor.UserScreens;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePhoneFragment extends Fragment {


    public ChangePhoneFragment() {
        // Required empty public constructor
    }

    EditText edt_otp;
    String code, name , emial, phone;
    String otp;

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    RelativeLayout btn_change_phone;

    TextView txt_endAgain;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    String userId,userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_phone, container, false);

        try{
            SharedPreferences sh =this.getActivity().getSharedPreferences("MyLogin",MODE_PRIVATE);
            userId = sh.getString("userId","");
            userType = sh.getString("userType","");

            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getUid() != null){
                mRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
            }else {
                mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            }

            txt_endAgain = view.findViewById(R.id.txt_sendAgain_MyProfile);

            Bundle b = getArguments();
            code = b.getString("code");
            Toast.makeText(Vendor.getAppContext(), code, Toast.LENGTH_SHORT).show();
            name = b.getString("name");
            phone = b.getString("phone");

            edt_otp = view.findViewById(R.id.edt_change_otp);

            btn_change_phone = view.findViewById(R.id.btn_change_number);
            btn_change_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeData();
                }
            });


            txt_endAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendAgain();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create View: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }






        return view;
    }


    public void sendAgain(){
        try {
            callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    // login user without sending otp
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    Toast.makeText(Vendor.getAppContext(), "verification failed", Toast.LENGTH_SHORT).show();

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(Vendor.getAppContext(), "Invalid phone number.", Toast.LENGTH_SHORT).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {

                        Toast.makeText(Vendor.getAppContext(), "Quota exceeded.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Toast.makeText(Vendor.getAppContext(), "code sent", Toast.LENGTH_SHORT).show();



                }
            };

            if (!phone.isEmpty()) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone,
                        60,
                        TimeUnit.SECONDS,
                        getActivity(),
                        callback);




            }
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Send Again: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void changeData(){

        try {

            //after changing number it goes to first screen

            otp = edt_otp.getText().toString().trim();
            PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(code, otp);
            mAuth.signInWithCredential(credentials).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    HashMap<String,Object> userUpdateDetails = new HashMap<>();
                    userUpdateDetails.put("UserName",name);

                    userUpdateDetails.put("UserPhone",phone);

                    mRef.updateChildren(userUpdateDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Vendor.getAppContext(), "Data Updated", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.v("phone",e.getMessage());
            Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
