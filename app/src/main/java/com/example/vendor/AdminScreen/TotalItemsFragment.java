package com.example.vendor.AdminScreen;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterTotalSummaryItemList;
import com.example.vendor.R;
import com.example.vendor.TotalSummaryConstructor;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalItemsFragment extends Fragment {


    DatabaseReference mRef;

    RecyclerView recyclerView;

    String currentDate, today;


    ArrayList<TotalSummaryConstructor> lists;
    AdapterTotalSummaryItemList adapter;

    RelativeLayout btn_exportToExcel;

    ValueEventListener valueEventListener;


    int WRITE_REQUEST_CODE = 100;

    DatabaseReference databaseReference;

    Button btn_select_a_date;
    TextView txt_date;
    String monthInString;
    DatePickerDialog.OnDateSetListener mDateSetListenr;


    HashMap<String,Double> totalSummary;

    DatabaseReference  mO, mOrder;



    ValueEventListener  valueEventListener1;



    String key;

    public TotalItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_items, container, false);


        try {
            recyclerView = view.findViewById(R.id.recycle__total_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            btn_exportToExcel = view.findViewById(R.id.btn_export_to_excel);
            btn_select_a_date = view.findViewById(R.id.btn_date_picker_item);
            txt_date = view.findViewById(R.id.txt_date_item);

            lists = new ArrayList<>();
            mO = FirebaseDatabase.getInstance().getReference().child("TotalOrder");
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order");
            mRef = FirebaseDatabase.getInstance().getReference().child("OrderContent");

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, WRITE_REQUEST_CODE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
            currentDate = sdf.format(new Date());
            today = currentDate.replace(".", "");



            txt_date.setText(""+currentDate);


            loadItemWise();


            btn_exportToExcel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String todayDate = txt_date.getText().toString();
                    today = todayDate.replace(".","");

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("TotalOrder").child(today);

                    File folder = new File(Environment.getExternalStorageDirectory() + "/VendorData");
                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                    if (success) {
                        // Do something on success

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                String ext = Environment.getExternalStorageDirectory().toString();
                                File file = new File(ext, "VendorData/"+todayDate+" itemWise.csv");
                                try {
                                    FileWriter fileWriter = new FileWriter(file);
                                    final CSVWriter writer = new CSVWriter(fileWriter);
                                    final String[] header = {"Order ON", "ItemName", "Qty"};
                                    writer.writeNext(header);

                                    for(DataSnapshot ds: snapshot.getChildren()){

                                        String itemName= ds.child("itemName").getValue().toString();
                                        String itemQuantiyt = ds.child("itemQuantity").getValue().toString();
                                        String itemUnit = ds.child("itemUnit").getValue().toString();

                                        Log.v("data",itemName+itemQuantiyt+itemUnit);

                                        List<String[]> data = new ArrayList<String[]>();
                                        data.add(new String[] {currentDate, itemName, itemQuantiyt+itemUnit });
                                        writer.writeAll(data);




                                    }


                                    writer.close();
                                    Toast.makeText(Vendor.getAppContext(), "ok", Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        // Do something else on failure
                    }








                }
            });
        }catch (Exception e){
            Log.v("TotalError",e.getMessage());
        }

        btn_select_a_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                monthInString = cal.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.getDefault());
                int month = cal.get(Calendar.MONDAY);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenr,
                        year,month,day
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date =  year + "." + monthInString + "." + day;
                currentDate = date;
                today =  date.replace(".", "");
                getData();

                loadItemWise();

                txt_date.setText(""+date);

            }
        };



        return view;
    }


    public void getData(){
        totalSummary = new HashMap<>();

        valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        final String keys = ds.getKey();
                        mOrder.child(keys).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()){
                                    for(DataSnapshot child : snapshot.getChildren()){

                                        key = child.getKey();
                                        Toast.makeText(Vendor.getAppContext(), key, Toast.LENGTH_SHORT).show();
//                                            //String status = child.child("orderStatus").getValue().toString();
                                        mRef.child(keys).child(key).addValueEventListener( new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    final String itemName = dataSnapshot.child("itemName").getValue().toString();
                                                    double itemQuantity = Double.parseDouble(dataSnapshot.child("itemQuantity").getValue().toString());
                                                    String itemUnit = dataSnapshot.child("itemUnit").getValue().toString();



                                                    try {
                                                        String itemGms = dataSnapshot.child("itemGms").getValue().toString();

                                                        if(Integer.parseInt(itemGms) > 0){
                                                            double itemGm = Double.parseDouble(itemGms) /1000;
                                                            itemQuantity = itemQuantity + itemGm;
                                                        }
                                                    }catch (Exception e){
                                                        Log.v("excep",""+e.getMessage());
                                                    }



                                                    Log.v("itemName",itemName);
                                                    Log.v("itemQuanity",Double.toString(itemQuantity));
                                                    if(totalSummary.containsKey(itemName)){
                                                        double mapQuantity = totalSummary.get(itemName);

                                                        final double totalQuantity = mapQuantity + itemQuantity;
                                                        totalSummary.put(itemName,totalQuantity);



                                                        Log.v("totalQu",Double.toString(totalQuantity));
                                                        final String finalItemUnit = itemUnit;
                                                        mO.child(today).child(itemName).child("itemName").setValue(itemName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mO.child(today).child(itemName).child("itemQuantity").setValue(Double.toString(totalQuantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        mO.child(today).child(itemName).child("itemUnit").setValue(finalItemUnit);
                                                                    }
                                                                }) ;
                                                            }
                                                        });

                                                        Log.v("hasmap",totalSummary.get(itemName).toString());
                                                        Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        totalSummary.put(itemName,itemQuantity);
                                                        // Log.v("hasmap",totalSummary.get(itemName).toString());
                                                        //  Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();

                                                        Log.v("itemQu",Double.toString(itemQuantity));
                                                        final double finalItemQuantity = itemQuantity;

                                                        final String finalItemUnit1 = itemUnit;
                                                        mO.child(today).child(itemName).child("itemName").setValue(itemName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mO.child(today).child(itemName).child("itemQuantity").setValue(Double.toString(finalItemQuantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        mO.child(today).child(itemName).child("itemUnit").setValue(finalItemUnit1);
                                                                    }
                                                                }) ;
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };



        mOrder.addValueEventListener(valueEventListener1);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted.
                    Toast.makeText(Vendor.getAppContext(), "granted", Toast.LENGTH_SHORT).show();

                } else {
                    //Denied.
                }
                break;
        }
    }


    public void loadItemWise() {



        lists.clear();

        mRef = FirebaseDatabase.getInstance().getReference().child("TotalOrder").child(today);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        TotalSummaryConstructor totalSummaryConstructor = child.getValue(TotalSummaryConstructor.class);
                        lists.add(totalSummaryConstructor);
                    }
                }

                adapter = new AdapterTotalSummaryItemList(getContext(), lists);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
