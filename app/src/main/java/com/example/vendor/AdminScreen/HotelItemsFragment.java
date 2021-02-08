package com.example.vendor.AdminScreen;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterCategoryOrders;
import com.example.vendor.Adapters.AdapterHotelList;
import com.example.vendor.Adapters.HolderHotelName;
import com.example.vendor.AdminHotelOrder;
import com.example.vendor.HotelNameContsnats;
import com.example.vendor.R;
import com.example.vendor.TotalHotelConstructor;
import com.example.vendor.Vendor;
import com.example.vendor.itemList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelItemsFragment extends Fragment {


    public HotelItemsFragment() {
        // Required empty public constructor
    }

    EditText edt_search;
    RecyclerView rl_gold, rl_silver, rl_bronze, rl_fixed, rl_guest;

    ArrayList<AdminHotelOrder> guest;


    DatabaseReference dbRef, databaseReference, orderContent, mHotelList, mProductPurchase, Hotel;

    String currentDate, today;
    String memberType, dataHotel;
    int counter = 0;

    ValueEventListener valueEventListener;
    ArrayList<AdminHotelOrder> listGold;
    ArrayList<AdminHotelOrder> listSilver;
    ArrayList<AdminHotelOrder> listBronze;
    ArrayList<AdminHotelOrder> listFixed;
    ArrayList<AdminHotelOrder> listGuest;
    AdapterHotelList adapter;

    HashMap<String, Double> totalSummary;


    Button btn_export;
    String monthInString;

    String orderId, orderTime, orderDate, hotelNames, memberShip, itemName, Qty, unit;

    HashSet<AdminHotelOrder> hashSet;
    // int counter = 0;

    int count = 0;
    boolean flag;

    ArrayList<TotalHotelConstructor> list;
    Button btn_datepicker;
    DatePickerDialog.OnDateSetListener mDateSetListenr;
    TextView txt_date;

    String dataHotelName;

    ArrayList<String> hotelList;

    ArrayList<String> dataList;


    ArrayList<String> itemQuantityList, itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_items, container, false);

        try {
            edt_search = view.findViewById(R.id.edt_hotelWiseSearch);
            rl_gold = view.findViewById(R.id.rl_gold);
            rl_bronze = view.findViewById(R.id.rl_bronze);
            rl_fixed = view.findViewById(R.id.rl_fixed);
            rl_silver = view.findViewById(R.id.rl_silver);
            rl_guest = view.findViewById(R.id.rl_guest);
            btn_export = view.findViewById(R.id.btn_export_to_excel_hotelList);
            btn_datepicker = view.findViewById(R.id.btn_hotel_items_date_picker);
            txt_date = view.findViewById(R.id.txt_hotel_item_date);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
            currentDate = sdf.format(new Date());// 12.06.1999
            today = currentDate.replace(".", "");

            //oredrContent
            orderContent = FirebaseDatabase.getInstance().getReference().child("OrderContent");

            totalSummary = new HashMap<>();


            //order
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");


            //hotelwiseList
            mHotelList = FirebaseDatabase.getInstance().getReference().child("HotelWiseList");


            rl_gold.setLayoutManager(new LinearLayoutManager(getContext()));
            rl_bronze.setLayoutManager(new LinearLayoutManager(getContext()));
            rl_fixed.setLayoutManager(new LinearLayoutManager(getContext()));
            rl_silver.setLayoutManager(new LinearLayoutManager(getContext()));
            rl_guest.setLayoutManager(new LinearLayoutManager(getContext()));


            showGoldHotels();
            showSilverHotels();
            showFixedHotels();
            showBronzeHotels();
            showGuest();
            loadData();
            txt_date.setText("" + currentDate);


            btn_export.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exportToExcel();

                }
            });

            //for date picker
            btn_datepicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    monthInString = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                    int month = cal.get(Calendar.MONDAY);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListenr,
                            year, month, day
                    );

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
            });


            mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = year + "." + monthInString + "." + day;
                    currentDate = date;
                    today = date.replace(".", "");
                    loadData();
                    showGoldHotels();
                    showBronzeHotels();
                    showFixedHotels();
                    showGuest();
                    showSilverHotels();

                    txt_date.setText("" + date);

                }
            };

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mProductPurchase = FirebaseDatabase.getInstance().getReference().child("ProductPurchase");
        Hotel = FirebaseDatabase.getInstance().getReference().child("Hotels");

        return view;
    }


    private void exportToExcel() {

//

//to export to the excel

//        read(new FirebaseListCallBackData() {
//            @Override
//            public void onListCallBackData(final List<String> itemListData) {
//                Log.v("itemData", "" + itemListData.toString());   "Cabbage","pataoy"
//                readData(new FirebaseCallback() {
//                    @Override
//                    public void onCallsback(List<String> list) {
//                        Log.v("hotelist", "" + list.toString());   "samraj","hdjhd"
//
//
//                        File folder = new File(Environment.getExternalStorageDirectory() + "/VendorData");
//                        boolean success = true;
//                        if (!folder.exists()) {
//                            success = folder.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            String ext = Environment.getExternalStorageDirectory().toString();
//                            final File file = new File(ext, "VendorData/" + txt_date.getText().toString() + "hotelWise.csv");
//
//                            dataList = new ArrayList<>();
//
//
//                            try {
//                                FileWriter fileWriter = new FileWriter(file);
//                                final CSVWriter writer = new CSVWriter(fileWriter);
//
//                                String[] list1 = new String[0];
//                                list1 = list.toArray(list1);
//
//                                Log.v("List1", "" + list1.length);
//
//
//                                writer.writeNext(list1); header
//
//
//                                                 items,samarj,sudha
//
//                                readItemData(new FirebaseListCallBack() {
//                                    @Override
//                                    public void onListCallBack(List<String> itemDataList) { "samraj","76kg", "gyfhfd","89kg"
//                                        Log.v("data", "" + itemDataList);
//
//                                        for (int i = 0; i < itemDataList.size(); i++) { samraj
//
//                                            dataList.add(itemDataList.get(i)); samraj
//
//                                            Log.v("items ", "" + itemDataList.get(i));
//                                            flag = true;
//                                            for (int hotels = 1; hotels < hotelList.size(); hotels++) { samarj,sudha
//
//
//                                                Log.v("hotelName", "" + hotelList.get(hotels));
//                                                for (int item = 0; item < itemListData.size(); item++) {
//
//
//                                                    if (itemListData.get(item).equals(hotelList.get(hotels))) {
//                                                        Log.v("quantss", "" + itemListData.get(item));
//
//                                                        dataList.add(itemListData.get(item + 1));
//
//                                                        Log.v("quantitty", "" + itemListData.get(item + 1));
//                                                            Log.v("data",itemListData.toString());
//                                                        flag = false;
//                                                        break;
//                                                    } else {
//                                                        item = item + 2;
//                                                    }
//                                                }
//
//                                                if (flag) {
//                                                    dataList.add("-");
//                                                }
//
//
//                                            }
//

//                                            String[] list2 = new String[0];
//
//                                            list2 = dataList.toArray(list2);
//                                            Log.v("itemList", "" + dataList.toString());
//
//                                            writer.writeNext(list2);
//
//                                            dataList.clear();
//
//
//                                        }
//
//
//                                    }
//                                });
//
//
//                                writer.close();
//                                Toast.makeText(Vendor.getAppContext(), "OK", Toast.LENGTH_SHORT).show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }
//                });
//            }
//        });






    }


    // load the data in firebase of hotelwise
    private void loadData() {


        try {
            dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
            count = 0;

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            final String keys = ds.getKey();

                            Log.v("keyHotel", "" + keys);

                            dbRef.child(keys).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {

                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            Log.v("size", Integer.toString(totalSummary.size()));
                                            // final String hotelName = child.child("HotelName").getValue().toString();
                                            //Log.v("hotelName", hotelName);
                                            final String key = child.getKey();


                                            Log.v("orderKey", "" + key);

                                            orderContent.child(keys).child(key).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    totalSummary.clear();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                        final String itemName = dataSnapshot.child("itemName").getValue().toString();
                                                        double itemQuantity = Double.parseDouble(dataSnapshot.child("itemQuantity").getValue().toString());
                                                        String itemUnit = dataSnapshot.child("itemUnit").getValue().toString();


                                                        mProductPurchase.child(itemName).child("itemName").setValue(itemName);

                                                        Hotel.child(keys).child("hotelName").setValue(keys);


                                                        try {
                                                            String itemGms = dataSnapshot.child("itemGms").getValue().toString();

                                                            if (Integer.parseInt(itemGms) > 0) {
                                                                double itemGm = Double.parseDouble(itemGms) / 1000;
                                                                itemQuantity = itemQuantity + itemGm;
                                                            }
                                                        } catch (Exception e) {
                                                            Log.v("excep", "" + e.getMessage());
                                                        }


                                                        Log.v("itemNameHotel", Double.toString(itemQuantity));
                                                        if (totalSummary.containsKey(itemName)) {
                                                            double mapQuantity = totalSummary.get(itemName);


                                                            final double totalQuantity = mapQuantity + itemQuantity;
                                                            totalSummary.put(itemName, totalQuantity);


                                                            Log.v("totalQuHotel", Double.toString(totalQuantity));
                                                            final String finalItemUnit = itemUnit;
                                                            final HashMap<String, Object> update = new HashMap<>();
                                                            update.put("itemName", itemName);
                                                            update.put("itemQuantity", Double.toString(totalQuantity));
                                                            update.put("itemUnit", finalItemUnit);

                                                            // mProductPurchase.child(itemName).child("itemCount").setValue(Integer.toString(count));

                                                            update.put("hotelName", keys);


                                                            Log.v("key", key);

                                                            mHotelList.child(today).child(itemName).child(keys).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            });


                                                            Log.v("hasmapHotel", totalSummary.get(itemName).toString());
                                                            Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            totalSummary.put(itemName, itemQuantity);
                                                            // Log.v("hasmap",totalSummary.get(itemName).toString());
                                                            //  Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();


                                                            Log.v("itemQu", Double.toString(itemQuantity));
                                                            final double finalItemQuantity = itemQuantity;
                                                            Log.v("key", key);

                                                            final String finalItemUnit1 = itemUnit;

                                                            final HashMap<String, Object> update = new HashMap<>();
                                                            update.put("itemName", itemName);
                                                            update.put("itemQuantity", Double.toString(finalItemQuantity));
                                                            update.put("itemUnit", finalItemUnit1);


                                                            update.put("hotelName", keys);


                                                            mHotelList.child(today).child(itemName).child(keys).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

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
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "show Items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null && dbRef != null) {
            dbRef.removeEventListener(valueEventListener);
        }
    }


    //firebase callback for itemQuantity list which creates list like, {"Sudha","45","Samraj","78"}
    private void read(final FirebaseListCallBackData firebaseListCallBackData) {

        dataList = new ArrayList<>();
        readData(new FirebaseCallback() {
            @Override
            public void onCallsback(final List<String> list) {
                Log.v("data", "" + list.toString());


                readItemData(new FirebaseListCallBack() {
                    @Override
                    public void onListCallBack(final List<String> itemLis) {
                        Log.v("quants", itemLis.toString());


                        itemQuantityList = new ArrayList<>();


                        for (int i = 0; i < itemLis.size(); i++) {
                            for (int j = 1; j < list.size(); j++) {

                                final int finalJ = j;
                                Log.v("listss", "" + list.get(j));
                                Log.v("listss", "" + itemLis.get(i));

                                mHotelList.child(txt_date.getText().toString().replace(".", "")).child(itemLis.get(i)).child(list.get(j)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChildren()) {

                                            String quant = snapshot.child("itemQuantity").getValue().toString();
                                            String unit = snapshot.child("itemUnit").getValue().toString();
                                            Log.v("jgig", "" + quant + unit);
                                            itemQuantityList.add(list.get(finalJ));
                                            itemQuantityList.add(quant + unit);

                                            Log.v("itemQ", "" + itemQuantityList.toString());


                                            firebaseListCallBackData.onListCallBackData(itemQuantityList);


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }


                        }


                    }
                });


            }
        });


    }


    //firebase callback for itemList, create list like {"Cabbage","Cauliflower"}
    private void readItemData(final FirebaseListCallBack firebaseListCallback) {

        itemList = new ArrayList<>();

        mProductPurchase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String dataItem = ds.getKey();
                        if (!itemList.contains(dataItem)) {
                            itemList.add(dataItem);
                        }


                    }
                    firebaseListCallback.onListCallBack(itemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //firebase callback for hotelList, create list like {"Sudha","Samraj"}
    private void readData(final FirebaseCallback firebaseCallback) {


        hotelList = new ArrayList<>();


        mHotelList.child(txt_date.getText().toString().replace(".", "")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    if (!hotelList.contains("Items")) {
                        hotelList.add("Items");
                    }

                    for (DataSnapshot ds : snapshot.getChildren()) {


                        for (DataSnapshot child : ds.getChildren()) {
                            String key = child.getKey();
                            Log.v("keys", "" + key);
                            if (!hotelList.contains(key)) {
                                hotelList.add(key);
                            }

                            Log.v("hotelListss", "" + hotelList.toString());
                        }


                    }

                    firebaseCallback.onCallsback(hotelList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Hotel.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.hasChildren()){
//                    hotelList.add("Items");
//                    for(DataSnapshot ds : snapshot.getChildren()){
//                        dataHotel = ds.getKey();
//                        hotelList.add(dataHotel);
//                    }
//
//                    Log.v("hotelData",""+hotelList.toString());
//                    firebaseCallback.onCallsback(hotelList);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private interface FirebaseListCallBackData {
        void onListCallBackData(List<String> itemList);
    }


    private interface FirebaseListCallBack {
        void onListCallBack(List<String> itemList);
    }

    private interface FirebaseCallback {
        void onCallsback(List<String> list);


    }


    //shows gold hotel list
    private void showGoldHotels() {

        listGold = new ArrayList<>();
        listGold.clear();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        dbRef.child(key).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        AdminHotelOrder orders = ds.getValue(AdminHotelOrder.class);
                                        memberType = orders.getMemberType();
                                        dataHotel = orders.getHotelName();
                                        if (memberType.equals("Gold")) {


                                            listGold.add(orders);

                                        }

                                        Set<AdminHotelOrder> a = new HashSet<AdminHotelOrder>();
                                        a.addAll(listGold);

                                        listGold = new ArrayList<>();
                                        listGold.addAll(a);
                                    }

                                    adapter = new AdapterHotelList(getContext(), listGold, currentDate);
                                    adapter.notifyDataSetChanged();
                                    rl_gold.setAdapter(adapter);
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

    //shows silver hotel list
    public void showSilverHotels() {
        listSilver = new ArrayList<>();
        listSilver.clear();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        dbRef.child(key).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        AdminHotelOrder orders = ds.getValue(AdminHotelOrder.class);
                                        memberType = orders.getMemberType();
                                        dataHotel = orders.getHotelName();
                                        if (memberType.equals("Silver")) {


                                            listSilver.add(orders);

                                        }

                                        Set<AdminHotelOrder> a = new HashSet<AdminHotelOrder>();
                                        a.addAll(listSilver);

                                        listSilver = new ArrayList<>();
                                        listSilver.addAll(a);
                                    }

                                    adapter = new AdapterHotelList(getContext(), listSilver, currentDate);
                                    adapter.notifyDataSetChanged();
                                    rl_silver.setAdapter(adapter);
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


    //shows bronze hotel list
    public void showBronzeHotels() {
        listBronze = new ArrayList<>();
        listBronze.clear();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        dbRef.child(key).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        AdminHotelOrder orders = ds.getValue(AdminHotelOrder.class);
                                        memberType = orders.getMemberType();
                                        dataHotel = orders.getHotelName();
                                        if (memberType.equals("Bronze")) {


                                            listBronze.add(orders);

                                        }

                                        Set<AdminHotelOrder> a = new HashSet<AdminHotelOrder>();
                                        a.addAll(listBronze);

                                        listBronze = new ArrayList<>();
                                        listBronze.addAll(a);
                                    }

                                    adapter = new AdapterHotelList(getContext(), listBronze, currentDate);
                                    adapter.notifyDataSetChanged();
                                    rl_bronze.setAdapter(adapter);
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

    //shows guest hotel list
    public void showGuest() {
        listGuest = new ArrayList<>();
        listGuest.clear();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        dbRef.child(key).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        AdminHotelOrder orders = ds.getValue(AdminHotelOrder.class);
                                        memberType = orders.getMemberType();
                                        dataHotel = orders.getHotelName();
                                        if (memberType.equalsIgnoreCase("Guest")) {


                                            listGuest.add(orders);

                                        }

                                        Set<AdminHotelOrder> a = new HashSet<AdminHotelOrder>();
                                        a.addAll(listGuest);

                                        listGuest = new ArrayList<>();
                                        listGuest.addAll(a);
                                    }

                                    adapter = new AdapterHotelList(getContext(), listGuest, currentDate);
                                    adapter.notifyDataSetChanged();
                                    rl_guest.setAdapter(adapter);
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


    //shows fix hotel list
    public void showFixedHotels() {
        listFixed = new ArrayList<>();
        listFixed.clear();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Order");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key = ds.getKey();
                        dbRef.child(key).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        AdminHotelOrder orders = ds.getValue(AdminHotelOrder.class);
                                        memberType = orders.getMemberType();
                                        dataHotel = orders.getHotelName();
                                        if (memberType.equals("Fix")) {


                                            listFixed.add(orders);

                                        }

                                        Set<AdminHotelOrder> a = new HashSet<AdminHotelOrder>();
                                        a.addAll(listFixed);

                                        listFixed = new ArrayList<>();
                                        listFixed.addAll(a);
                                    }

                                    adapter = new AdapterHotelList(getContext(), listFixed, currentDate);
                                    adapter.notifyDataSetChanged();
                                    rl_fixed.setAdapter(adapter);
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
