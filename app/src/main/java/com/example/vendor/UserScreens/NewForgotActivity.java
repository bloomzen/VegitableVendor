package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewForgotActivity extends AppCompatActivity {

    EditText edt_newPass, edt_conPass;
    RelativeLayout btn_change;
    String newPass, conPass;
    DatabaseReference dbRef;


    //forgot password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forgot);


        try{
            edt_newPass = findViewById(R.id.edt_forgot_newPassword);
            edt_conPass = findViewById(R.id.edt_forgot_ConfirmPassword);
            btn_change = findViewById(R.id.btn_verify_forgot_password);




            btn_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newPass = edt_newPass.getText().toString();
                    conPass = edt_conPass.getText().toString();

                    if(!newPass.isEmpty() && !conPass.isEmpty()){
                        if(newPass.equals(conPass)){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getUid());
                            dbRef.child("UserPassword").setValue(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(NewForgotActivity.this, "updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NewForgotActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                }
            });

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
