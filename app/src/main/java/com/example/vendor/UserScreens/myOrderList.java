package com.example.vendor.UserScreens;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapetMyOrderGuest;
import com.example.vendor.Adapters.AdapetMyOrderMember;
import com.example.vendor.Adapters.AdapterCartData;
import com.example.vendor.Adapters.AdapterCartDataGuest;
import com.example.vendor.Adapters.AdapterMyOrderItem;
import com.example.vendor.Adapters.AdapterMySummaryMember;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.CartData;
import com.example.vendor.MyOrderData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.orderItemData;
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
public class myOrderList extends Fragment {


    public myOrderList() {
        // Required empty public constructor
    }


    //show my orders

    ArrayList<MyOrderData> cartDataArrayListMyOrder;
    AdapetMyOrderMember adapterCartDataMyOrder;

    ArrayList<MyOrderData> cartDataArrayListMyOrderGuest;
    AdapetMyOrderGuest adapterCartDataMyOrderGuest;

    ArrayList<MyOrderData> cartDataArrayListMyOrderSummary;
    AdapterMySummaryMember adapterMySummaryMember;





    RecyclerView recyclerViewListItem, recyclerViewListSummary;
    ArrayList<orderItemData> myOrderList;
    AdapterMyOrderItem adapterMyOrderItem;
    TextView guestTotalPriceMyOrder;

    DatabaseReference mRefMember;
    String orderNumber, HotelName, memberType, total_amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order_list, container, false);

        try {

            recyclerViewListItem = view.findViewById(R.id.recyclerCartMyOrder);
            recyclerViewListSummary = view.findViewById(R.id.recyclerCartItemSummaryMyOrder);
            guestTotalPriceMyOrder = view.findViewById(R.id.guestTotalPriceMyOrder);

            mRefMember = FirebaseDatabase.getInstance().getReference().child("OrderContent");

            //recyclerView = view.findViewById(R.id.recyclerOrderItem);
            recyclerViewListSummary.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewListItem.setLayoutManager(new LinearLayoutManager(getContext()));
            Bundle bundle = getArguments();
            orderNumber = bundle.getString("orderNumber");
            HotelName = bundle.getString("hotelName");
            memberType = bundle.getString("memberType");
            total_amount = bundle.getString("totalAmount");


        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        if (memberType.equalsIgnoreCase("Gold") || memberType.equalsIgnoreCase("Silver") || memberType.equalsIgnoreCase("Bronze")
                || memberType.equalsIgnoreCase("Fix")) {
            guestTotalPriceMyOrder.setVisibility(View.GONE);
            loadMemberProducts();
            loadMemberSummaryProducts();


        } else if (memberType.equalsIgnoreCase("guest")) {
            guestTotalPriceMyOrder.setText("Total Price: "+total_amount);
            loadGuestProducts();
            loadMemberSummaryProducts();

        }


        return view;
    }



    private void loadMemberSummaryProducts() {
    cartDataArrayListMyOrderSummary = new ArrayList<>();
        mRefMember.child(HotelName).child(orderNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MyOrderData orderData = ds.getValue(MyOrderData.class);
                        cartDataArrayListMyOrderSummary.add(orderData);
                    }

                    adapterMySummaryMember = new AdapterMySummaryMember(getContext(), cartDataArrayListMyOrderSummary);
                    recyclerViewListSummary.setAdapter(adapterMySummaryMember);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadGuestProducts() {
        cartDataArrayListMyOrderGuest = new ArrayList<>();
        mRefMember.child(HotelName).child(orderNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MyOrderData orderData = ds.getValue(MyOrderData.class);
                        cartDataArrayListMyOrderGuest.add(orderData);
                    }

                    adapterCartDataMyOrderGuest = new AdapetMyOrderGuest(getContext(), cartDataArrayListMyOrderGuest);
                    recyclerViewListItem.setAdapter(adapterCartDataMyOrderGuest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadMemberProducts() {
        cartDataArrayListMyOrder = new ArrayList<>();
        mRefMember.child(HotelName).child(orderNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        MyOrderData orderData = ds.getValue(MyOrderData.class);
                        cartDataArrayListMyOrder.add(orderData);
                    }

                    adapterCartDataMyOrder = new AdapetMyOrderMember(getContext(), cartDataArrayListMyOrder);
                    recyclerViewListItem.setAdapter(adapterCartDataMyOrder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
