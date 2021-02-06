package com.example.vendor.UserScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.AdminScreen.AdminLogin;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText edt_login_email, edt_login_password;
    RelativeLayout btn_login;
    TextView txt_forgotPassword, txt_createAccount;

    String email_phone, password;
    DatabaseReference databaseReference;

    String EmailParentNode;
    ProgressDialog mProgress;

    ImageView immageAdmin;


    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        try {
            edt_login_email = findViewById(R.id.edt_login_email);
            edt_login_password = findViewById(R.id.edt_login_password);
            btn_login = findViewById(R.id.btn_login);
            txt_forgotPassword = findViewById(R.id.txt_forgetPasswordLogin);
            txt_createAccount = findViewById(R.id.txt_createAccount);
            immageAdmin = findViewById(R.id.image_admin);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Please Wait");
            mProgress.setCanceledOnTouchOutside(false);

            immageAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                }
            });

            txt_createAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            });

            txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                }
            });

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_user();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void login_user() {

        try {
            mProgress.setMessage("Checking the credentials...");
            mProgress.show();

            email_phone = edt_login_email.getText().toString();
            password = edt_login_password.getText().toString();


            if (!email_phone.isEmpty() && !password.isEmpty()) {
                if (TextUtils.isDigitsOnly(email_phone) && email_phone.length() >= 10) {




                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getChildrenCount() > 0) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    mProgress.dismiss();
                                    final String userId = child.getKey();



                                    databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String pass = snapshot.child("UserPassword").getValue().toString();
                                            String userType = snapshot.child("UserType").getValue().toString();

                                            if (pass.equals(password)) {
                                                mProgress.dismiss();
                                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                SharedPreferences sh = getSharedPreferences("MyLogin", MODE_PRIVATE);


                                                SharedPreferences.Editor myEdit = sh.edit();
                                                myEdit.putString("userId", userId);
                                                myEdit.putString("userType",userType);

                                                Log.v("userId",userId);
                                                Log.v("userType",userType);


                                                myEdit.commit();
                                                Intent loginIntent = new Intent(getApplicationContext(), slider.class);

                                                startActivity(loginIntent);
                                                finish();
                                            } else {
                                                mProgress.hide();
                                                Toast.makeText(getApplicationContext(), "wrong Password ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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
                    };
                    databaseReference.orderByChild("UserPhone").equalTo(email_phone).addValueEventListener(valueEventListener);

                }else {
                    mProgress.hide();
                    Toast.makeText(this, "please input right credential", Toast.LENGTH_SHORT).show();
                }
            } else {
                mProgress.hide();
                Toast.makeText(this, "Empty fields...", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "login: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (databaseReference != null && valueEventListener != null) {
                databaseReference.removeEventListener(valueEventListener);
            }
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Destroy: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
