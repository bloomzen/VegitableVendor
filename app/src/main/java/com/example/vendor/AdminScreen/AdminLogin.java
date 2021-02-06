package com.example.vendor.AdminScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.UserScreens.ForgotPassword;
import com.example.vendor.Vendor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class
AdminLogin extends AppCompatActivity {

    ImageButton btn_back;
    TextView txt_forgotPassword;
    EditText edt_email,edt_password;
    RelativeLayout btn_login;

    DatabaseReference mAdminLogin;

    ProgressDialog mProgress;


    String email_phone,password;
    String  userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        try {
            btn_back = findViewById(R.id.btn_login_admin_back);
            txt_forgotPassword = findViewById(R.id.txt_forgetPasswordLoginAdmin);
            edt_email = findViewById(R.id.edt_login_admin_email);
            edt_password = findViewById(R.id.edt_login_admin_password);
            btn_login = findViewById(R.id.btn_login_admin);

            mAdminLogin = FirebaseDatabase.getInstance().getReference().child("Admin");
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Please wait...");
            mProgress.setCanceledOnTouchOutside(false);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginAdmin();
                }
            });

            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });

            txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent adminForgot = new Intent(getApplicationContext(), ForgotPassword.class);
                    adminForgot.putExtra("admin",1);
                    startActivity(adminForgot);
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Admin Login: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void loginAdmin() {

        try{
            mProgress.setMessage("Checking the credentials...");
            mProgress.show();

            email_phone = edt_email.getText().toString();
            password = edt_password.getText().toString();

            if (!email_phone.isEmpty() && !password.isEmpty()) {
                if (TextUtils.isDigitsOnly(email_phone) && email_phone.length() >= 10) {
                    mAdminLogin.orderByChild("UserPhone").equalTo(email_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    mProgress.dismiss();
                                    userId = child.getKey();

                                    mAdminLogin.child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String pass = snapshot.child("UserPassword").getValue().toString();
                                            if (pass.equals(password)) {
                                                mProgress.dismiss();
                                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                SharedPreferences sh = getSharedPreferences("MyAdminLogin", MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sh.edit();
                                                myEdit.putString("adminId", userId);
                                                myEdit.apply();
                                                Intent loginIntent = new Intent(getApplicationContext(), AdminHomeActivity.class);
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
                    });
                }else {
                    mProgress.hide();
                    Toast.makeText(this, "please input right credential", Toast.LENGTH_SHORT).show();
                }
            } else {
                mProgress.hide();
                Toast.makeText(this, "Empty fields...", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "login Admin: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
