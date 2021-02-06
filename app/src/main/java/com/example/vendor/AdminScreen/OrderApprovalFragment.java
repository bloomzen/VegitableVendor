package com.example.vendor.AdminScreen;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import com.example.vendor.Adapters.AdapterCategoryOrders;
import com.example.vendor.Adapters.HotelOrderHolder;
import com.example.vendor.AdminHotelOrder;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderApprovalFragment extends Fragment {


    public OrderApprovalFragment() {
        // Required empty public constructor
    }

    View view;
    EditText edt_search_pending;
    RecyclerView recyclerViewPending;


    //    FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder> adapter;
//    FirebaseRecyclerOptions<AdminHotelOrder> options;
    DatabaseReference mOrder;

    String member, hotelName;


    ArrayList<AdminHotelOrder> adminHotelOrders;
    AdapterCategoryOrders adapterCategoryOrders;

    CharSequence searchTxt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_approval, container, false);


        try {
            edt_search_pending = view.findViewById(R.id.edt_search_approve);
            recyclerViewPending = view.findViewById(R.id.recycler_approve);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerViewPending.setLayoutManager(layoutManager);
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order");

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
            Toast.makeText(Vendor.getAppContext(), "Approve: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
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
            Toast.makeText(Vendor.getAppContext(), "search: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();

                            mOrder.child(key).orderByChild("orderStatus").equalTo("Approve").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {
                                        try {
                                            for (DataSnapshot child : snapshot.getChildren()) {

                                                String key = child.getKey();
                                                Log.v("keys", "" + key);
                                                Log.v("order", child.getValue(AdminHotelOrder.class).toString());
                                                Toast.makeText(getContext(), child.getValue(AdminHotelOrder.class).toString(), Toast.LENGTH_SHORT).show();
                                                AdminHotelOrder FruitProduct = child.getValue(AdminHotelOrder.class);
                                                Log.v("fruit", FruitProduct.toString());
                                                adminHotelOrders.add(FruitProduct);

                                            }
                                        }catch (Exception e){
                                            Log.v("exce",""+e.getMessage());
                                        }

                                        adapterCategoryOrders = new AdapterCategoryOrders(getContext(), adminHotelOrders);
                                        adapterCategoryOrders.notifyDataSetChanged();
                                        recyclerViewPending.setAdapter(adapterCategoryOrders);

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
            Toast.makeText(Vendor.getAppContext(), "Order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


//        Query query = mOrder.orderByChild("orderStatus").equalTo("Approve");
//        options = new FirebaseRecyclerOptions.Builder<AdminHotelOrder>().setQuery(query, AdminHotelOrder.class).setLifecycleOwner(this).build();
//        adapter = new FirebaseRecyclerAdapter<AdminHotelOrder, HotelOrderHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull HotelOrderHolder holder, int position, @NonNull final AdminHotelOrder model) {
//                holder.txt_hotelNumber.setText("Order ID:"+""+model.getOrderNumber());
//                holder.txt_hotelTime.setText("Order ON:"+""+model.getOrderDate()+"\n"+model.getOrderTime());
//                holder.btn_status.setText(""+model.getOrderStatus());
//
//                member = model.getMemberType();
//                hotelName = model.getHotelName();
//                holder.btn_status.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
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
//        recyclerViewPending.setAdapter(adapter);


    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


}
