package com.example.vendor.AdminScreen;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterCartData;
import com.example.vendor.Adapters.AdapterCategoryOrders;
import com.example.vendor.Adapters.AdapterGuestUser;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.Adapters.HotelOrderHolder;
import com.example.vendor.AdminHotelOrder;
import com.example.vendor.CartData;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.UserScreens.HomeFragment;
import com.example.vendor.UserScreens.categoriesItemFragment;
import com.example.vendor.Vendor;
import com.example.vendor.gusteContentData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class orderPendingFragment extends Fragment {


    public orderPendingFragment() {
        // Required empty public constructor
    }

    View view;
    EditText edt_search_pending;
    RecyclerView recyclerViewPending;
    Button btn_total_summary;

    ArrayList<AdminHotelOrder> adminHotelOrders;
    AdapterCategoryOrders adapterCategoryOrders;

    //    FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder> adapter;
//    FirebaseRecyclerOptions<AdminHotelOrder> options;
    DatabaseReference mOrder;

    String member, hotelName;
    CharSequence searchTxt;

    HashMap<String, Double> totalSummary;

    DatabaseReference mRef, mO;


    ValueEventListener valueEventListener1;


    String currentDate, today;
    String key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_pending, container, false);


        try {
            edt_search_pending = view.findViewById(R.id.edt_search_pending);
            recyclerViewPending = view.findViewById(R.id.recycler_pending);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerViewPending.setLayoutManager(layoutManager);
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
            currentDate = sdf.format(new Date());
            today = currentDate.replace(".", "");
            mO = FirebaseDatabase.getInstance().getReference().child("TotalOrder");

            totalSummary = new HashMap<>();


            mRef = FirebaseDatabase.getInstance().getReference().child("OrderContent");


            valueEventListener1 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            final String keys = ds.getKey();
                            mOrder.child(keys).orderByChild("orderDate").equalTo(currentDate).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {
                                        for (DataSnapshot child : snapshot.getChildren()) {

                                            key = child.getKey();
                                            Toast.makeText(Vendor.getAppContext(), key, Toast.LENGTH_SHORT).show();
//                                            //String status = child.child("orderStatus").getValue().toString();
                                            mRef.child(keys).child(key).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        final String itemName = dataSnapshot.child("itemName").getValue().toString();
                                                        double itemQuantity = Double.parseDouble(dataSnapshot.child("itemQuantity").getValue().toString());
                                                        String itemUnit = dataSnapshot.child("itemUnit").getValue().toString();

                                                        try {
                                                            String itemGms = dataSnapshot.child("itemGms").getValue().toString();

                                                            if (Integer.parseInt(itemGms) > 0) {
                                                                double itemGm = Double.parseDouble(itemGms) / 1000;
                                                                itemQuantity = itemQuantity + itemGm;
                                                            }
                                                        } catch (Exception e) {
                                                            Log.v("excep", "" + e.getMessage());
                                                        }


                                                        Log.v("itemName", itemName);
                                                        Log.v("itemQuanity", Double.toString(itemQuantity));
                                                        if (totalSummary.containsKey(itemName)) {
                                                            double mapQuantity = totalSummary.get(itemName);

                                                            final double totalQuantity = mapQuantity + itemQuantity;
                                                            totalSummary.put(itemName, totalQuantity);


                                                            Log.v("totalQu", Double.toString(totalQuantity));
                                                            final String finalItemUnit = itemUnit;
                                                            mO.child(today).child(itemName).child("itemName").setValue(itemName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mO.child(today).child(itemName).child("itemQuantity").setValue(Double.toString(totalQuantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            mO.child(today).child(itemName).child("itemUnit").setValue(finalItemUnit);
                                                                        }
                                                                    });
                                                                }
                                                            });

                                                            Log.v("hasmap", totalSummary.get(itemName).toString());
                                                            Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            totalSummary.put(itemName, itemQuantity);
                                                            // Log.v("hasmap",totalSummary.get(itemName).toString());
                                                            //  Toast.makeText(Vendor.getAppContext(), totalSummary.get(itemName).toString(), Toast.LENGTH_SHORT).show();

                                                            Log.v("itemQu", Double.toString(itemQuantity));
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
                                                                    });
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


            btn_total_summary = view.findViewById(R.id.btn_total_summary);

            btn_total_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TotalSummaryFragment hotelOrderFragment = new TotalSummaryFragment();

                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).commit();

                }
            });

            edt_search_pending.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    try {
                        searchTxt = s;
                        search(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        searchTxt = s;
                        search(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            getAllOrders();
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Pending: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mOrder != null && valueEventListener1 != null) {
            mOrder.removeEventListener(valueEventListener1);
        }
    }

    private void search(CharSequence s) {


        try {
            ArrayList<AdminHotelOrder> myList = new ArrayList<>();
            for (AdminHotelOrder object : adminHotelOrders) {
                if (object.getOrderNumber().toLowerCase().contains(s)) {
                    myList.add(object);
                }
            }

            adapterCategoryOrders = new AdapterCategoryOrders(getContext(), myList);
            recyclerViewPending.setAdapter(adapterCategoryOrders);

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Pend: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }





    private void getAllOrders() {

        try {
            adminHotelOrders = new ArrayList<>();
            adminHotelOrders.clear();

            mOrder.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChildren()) {
                        Toast.makeText(Vendor.getAppContext(), String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();

                        for (DataSnapshot ds : snapshot.getChildren()) {

                            final String key = ds.getKey();
                            Log.v("key", "" + key);

                            mOrder.child(key).orderByChild("orderStatus").equalTo("Pending").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {


                                        //Toast.makeText(Vendor.getAppContext(), ""+snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

                                        Log.v("snapshot",""+snapshot.toString());
                                        try {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                String k = child.getKey();
                                                Log.v("k", "" + k);
                                                AdminHotelOrder FruitProduct = child.getValue(AdminHotelOrder.class);
                                                Log.v("fruit", FruitProduct.getHotelName().toString());
                                                adminHotelOrders.add(FruitProduct);
                                            }

                                        } catch (Exception e) {
                                            Log.v("exce", "" + e.getMessage());
                                        }
                                    }

                                    adapterCategoryOrders = new AdapterCategoryOrders(getContext(), adminHotelOrders);
                                    adapterCategoryOrders.notifyDataSetChanged();
                                    recyclerViewPending.setAdapter(adapterCategoryOrders);
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
            Toast.makeText(Vendor.getAppContext(), "Orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


//        Query query = mOrder.orderByChild("orderStatus").equalTo("Pending");
//        options = new FirebaseRecyclerOptions.Builder<AdminHotelOrder>().setQuery(query, AdminHotelOrder.class).setLifecycleOwner(this).build();
//        adapter = new FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull HotelOrderHolder holder, int position, @NonNull final AdminHotelOrder model) {
//                holder.txt_hotelNumber.setText("Order ID:"+""+model.getOrderNumber());
//                holder.txt_hotelTime.setText("Order ON:"+""+model.getOrderDate()+"\n"+model.getOrderTime());
//                holder.btn_status.setText(""+model.getOrderStatus());
//
//                 member = model.getMemberType();
//                 hotelName = model.getHotelName();
//                holder.btn_status.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//
//                        AdminViewHotelOrderFragment hotelOrderFragment = new AdminViewHotelOrderFragment();
//
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("hotelName",hotelName);
//                        bundle.putString("orderID",model.getOrderNumber());
//                        bundle.putString("orderON",model.getOrderDate()+"\n"+model.getOrderTime());
//                        bundle.putString("member",member);
//                        bundle.putString("status",model.getOrderStatus());
//                        hotelOrderFragment.setArguments(bundle);
//
//                        fragmentTransaction.replace(R.id.fragment_Admin_container,hotelOrderFragment).commit();
//
//
//
//
//
//
//
//
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public HotelOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.order_list, parent, false);
//
//                return new HotelOrderHolder(view);
//            }
//        };
//
//      recyclerViewPending.setAdapter(adapter);


    }


}
