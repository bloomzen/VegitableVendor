package com.example.vendor.AdminScreen;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterLogData;
import com.example.vendor.LogData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelPaymentFragment extends Fragment {


    public HotelPaymentFragment() {
        // Required empty public constructor
    }

    TextView txt_totalOrder, txt_totalBalance;
    RelativeLayout rl_paid;
    DatabaseReference dbTotalBalance;


    String hotelName, member;

    EditText edittext_amountPayed;
    AlertDialog alertdialog;
    String amount_payed;
    ImageView imgBack;
    AlertDialog.Builder builder;
    LayoutInflater layoutinflater;

    String totalBalance;

    String today,currentDate;
    double afterDeduct;


    ArrayList<LogData> logDataList;
    AdapterLogData adapterLogData;
    RecyclerView rec_log;
    int counter = 0;
    int count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_payment, container, false);
        txt_totalOrder = view.findViewById(R.id.txt_TotalOrderNumber);
        txt_totalBalance = view.findViewById(R.id.txt_TotalPayment);
        rl_paid = view.findViewById(R.id.btn_payment_paid);


        SharedPreferences sh = this.getActivity().getSharedPreferences("MyHotelDetails", MODE_PRIVATE);
        hotelName = sh.getString("HotelName", "");
        member = sh.getString("memberShip", "");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
        currentDate = sdf.format(new Date());
        today = currentDate.replace(".", "");
        rec_log = view.findViewById(R.id.recycler_log);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Vendor.getAppContext());

        rec_log.setLayoutManager(layoutManager);





        Toast.makeText(Vendor.getAppContext(), "" + hotelName + member, Toast.LENGTH_SHORT).show();


        dbTotalBalance = FirebaseDatabase.getInstance().getReference().child("Payment");



        dbTotalBalance.child(hotelName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    try {
                        String totalOrder = snapshot.child("OrderNumber").getValue().toString();
                        totalBalance = snapshot.child("payment").getValue().toString();
                        txt_totalOrder.setText("" + totalOrder);
                        txt_totalBalance.setText("Rs " + totalBalance);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        rl_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);

                    layoutinflater = getLayoutInflater();

                    View Dview = layoutinflater.inflate(R.layout.payment_edit, null);

                    builder.setCancelable(false);

                    builder.setView(Dview);

                    edittext_amountPayed = Dview.findViewById(R.id.edt_payment);
                    imgBack = Dview.findViewById(R.id.img_cancel);


                    alertdialog = builder.create();


                    edittext_amountPayed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if ((actionId == EditorInfo.IME_ACTION_DONE) ) {
                                Log.v("pressed", "Enter pressed");
                                try {
                                    alertdialog.dismiss();
                                    amount_payed = edittext_amountPayed.getText().toString();
                                    if(Double.parseDouble(amount_payed) > Double.parseDouble(totalBalance) && Double.parseDouble(amount_payed) <= 0 ){
                                        Toast.makeText(Vendor.getAppContext(), "Can't be dudycted", Toast.LENGTH_SHORT).show();
                                    }else {
                                        afterDeduct = Double.parseDouble(totalBalance) - Double.parseDouble(amount_payed);

                                        dbTotalBalance.child(hotelName).child("Log").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                counter =(int) snapshot.getChildrenCount() ;
                                                Log.v("counter",""+Integer.toString(counter));


                                                count = counter + 1;
                                                Log.v("count",""+Integer.toString(count));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        dbTotalBalance.child(hotelName).child("payment").setValue(String.valueOf(afterDeduct)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Vendor.getAppContext(), "Amount Deducted", Toast.LENGTH_SHORT).show();


                                                HashMap<String,Object> log = new HashMap<>();
                                                log.put("date",currentDate);
                                                log.put("amount",amount_payed);
                                                dbTotalBalance.child(hotelName).child("Log").child(String.valueOf(count)).setValue(log).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Vendor.getAppContext(), "logged", Toast.LENGTH_SHORT).show();
                                                         dbTotalBalance.child(hotelName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                 String payment = snapshot.child("payment").getValue().toString();
                                                                 txt_totalBalance.setText("Rs "+payment);
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError error) {

                                                             }
                                                         });
                                                         getData();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Vendor.getAppContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }
                                        });
                                    }

                                }catch (Exception e){
                                    Log.v("ex",e.getMessage());
                                }




                            }else {
                                alertdialog.dismiss();
                            }
                            return false;
                        }
                    });


                    imgBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertdialog.dismiss();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

                alertdialog.show();


            }
        });

        getData();

        return view;
    }


    public void  getData(){
        logDataList = new ArrayList<>();
        logDataList.clear();

        dbTotalBalance.child(hotelName).child("Log").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String key = ds.getKey();
                        Log.v("keys",""+key);
                        LogData logDatas = ds.getValue(LogData.class);
                        logDataList.add(logDatas);
                    }

                    adapterLogData = new AdapterLogData(getContext(),logDataList);
                    adapterLogData.notifyDataSetChanged();
                    rec_log.setAdapter(adapterLogData);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
