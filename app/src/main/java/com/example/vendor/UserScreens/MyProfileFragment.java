package com.example.vendor.UserScreens;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {


    public MyProfileFragment() {
        // Required empty public constructor
    }

    EditText edt_profileName, edt_profilePhone, edt_profileEmail;
    RelativeLayout rl_change;
    String email, name, phone;

    Context context;
    MyProfileFragment fragment;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;



    //change name and phone number

    String userId, userType;

    DatabaseReference mRef;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);



        try{
            SharedPreferences sh =this.getActivity().getSharedPreferences("MyLogin",MODE_PRIVATE);
            userId = sh.getString("userId","");
            userType = sh.getString("userType","");

            mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            edt_profilePhone = view.findViewById(R.id.edt_myProfilePhone);
            edt_profileName = view.findViewById(R.id.edt_myProfileName);
            rl_change = view.findViewById(R.id.btn_change_next);


            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("UserName").getValue().toString();
                    String phone = snapshot.child("UserPhone").getValue().toString();

                    edt_profileName.setText(""+name);
                    edt_profilePhone.setText(""+phone);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            fragment = new MyProfileFragment();






            rl_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = edt_profileName.getText().toString().trim();
                    phone = edt_profilePhone.getText().toString().trim();




                    onAuth();






                }
            });


        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }







        return view;
    }

    public void onAuth(){

//        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone,
//                60,
//                TimeUnit.SECONDS,
//                getActivity(),
//                callback);

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

                    ChangePhoneFragment ldf = new ChangePhoneFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();

                    Toast.makeText(Vendor.getAppContext(), s, Toast.LENGTH_SHORT).show();

                    Bundle bundle=new Bundle();
                    bundle.putString("code",s);
                    bundle.putString("name",name);

                    bundle.putString("phone",phone);

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_container, ldf);
                    transection.commit();


                }
            };


            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone,
                    60,
                    TimeUnit.SECONDS,
                    getActivity(),
                    callback);

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Auth: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
