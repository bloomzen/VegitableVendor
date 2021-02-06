package com.example.vendor.UserScreens;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    EditText edt_currentPassword, edt_newPassword, edt_confirmPassword;
    RelativeLayout rl_change;
    DatabaseReference mRef;

    String Currentpassword, NewPassword, ConfirmPassword;

    FirebaseAuth mAuth;
    String userId,userType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_change_password, container, false);

       try{
           SharedPreferences sh =this.getActivity().getSharedPreferences("MyLogin",MODE_PRIVATE);
           userId = sh.getString("userId","");
           userType = sh.getString("userType","");
           edt_currentPassword = view.findViewById(R.id.edt_currentPassword);
           edt_newPassword = view.findViewById(R.id.edt_newPassword);
           edt_confirmPassword = view.findViewById(R.id.edt_confirmPassword);
           rl_change = view.findViewById(R.id.btn_change_password);

           mRef = FirebaseDatabase.getInstance().getReference().child("User");

           mAuth = FirebaseAuth.getInstance();

           if(mAuth.getUid() != null){
               mRef.child(mAuth.getUid());

           }else {
               mRef.child(userId);
           }

       }catch (Exception e){
           Toast.makeText(Vendor.getAppContext(), "On Create: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
       }


       // change password

        try {
            rl_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String password = snapshot.child("UserPassword").getValue().toString();
                            Currentpassword = edt_currentPassword.getText().toString().trim();
                            NewPassword = edt_newPassword.getText().toString().trim();
                            ConfirmPassword = edt_confirmPassword.getText().toString().trim();

                            if(password.equals(Currentpassword)){
                                if(NewPassword.length() >= 8){
                                    if(NewPassword.equals(ConfirmPassword)){
                                        HashMap<String,Object> Haspassword = new HashMap<>();
                                        Haspassword.put("UserPassword",NewPassword);
                                        mRef.updateChildren(Haspassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Vendor.getAppContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                                FragmentManager fragmentManager = getFragmentManager();


                                                FragmentTransaction transaction = null;
                                                if (fragmentManager != null) {
                                                    transaction = fragmentManager.beginTransaction();
                                                    transaction.setReorderingAllowed(true);

                                                    // Replace whatever is in the fragment_container view with this fragment
                                                    transaction.replace(R.id.fragment_container, new ProfileFragment(), null);

                                                    // Commit the transaction
                                                    transaction.commit();

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else {
                                        Toast.makeText(Vendor.getAppContext(), "password and confirm password doesn't match", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(Vendor.getAppContext(), "Password should be length minimum 8", Toast.LENGTH_SHORT).show();
                                }


                            }else if(!password.equals(Currentpassword)){
                                Toast.makeText(Vendor.getAppContext(), "Current Password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Change Password: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



       return view;
    }

}
