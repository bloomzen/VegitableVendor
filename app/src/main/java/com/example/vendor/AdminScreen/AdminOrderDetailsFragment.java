package com.example.vendor.AdminScreen;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.adminOrderPagerHolder;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.orderItemData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminOrderDetailsFragment extends Fragment {


    public AdminOrderDetailsFragment() {
        // Required empty public constructor
    }

    String hotelName, hotelID, hotelDateTime, member, status, totalAmount;

    TextView txt_hotelName, txt_orderNumber, txt_orderTime, txt_member, txt_total_Price_view_order;
    RecyclerView rl_item;

    FirebaseRecyclerAdapter<orderItemData, adminOrderPagerHolder> adapter;
    FirebaseRecyclerOptions<orderItemData> options;
    DatabaseReference mOrderContent, mOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_order_details, container, false);

        try{
            Bundle bundle = getArguments();
            hotelName = bundle.getString("hotelName","");
            hotelID = bundle.getString("orderID","");
            hotelDateTime = bundle.getString("orderOn","");
            member = bundle.getString("member","");
            String orderTime = bundle.getString("orderTime","");
            totalAmount = bundle.getString("totalPrice","");


            txt_hotelName = view.findViewById(R.id.txt_hotelName_view_orders);
            txt_orderNumber = view.findViewById(R.id.txt_orderIDNumber_view_order);
            txt_orderTime = view.findViewById(R.id.txt_orderONNumber_view_order);
            txt_member = view.findViewById(R.id.txt_memberType_orders);
            txt_total_Price_view_order = view.findViewById(R.id.txt_total_Price_view_order);
            rl_item = view.findViewById(R.id.recycler_pending_items_view_order);
            rl_item.setLayoutManager(new LinearLayoutManager(getContext()));


            txt_hotelName.setText(""+hotelName);
            txt_orderNumber.setText(""+hotelID);
            txt_orderTime.setText(""+hotelDateTime + "\n" + orderTime);
            txt_member.setText(""+member);
            txt_total_Price_view_order.setText("Total "+"Rs. "+ totalAmount);
            mOrderContent = FirebaseDatabase.getInstance().getReference().child("OrderContent").child(hotelName).child(hotelID);
            mOrder = FirebaseDatabase.getInstance().getReference().child("Order").child(hotelName).child(hotelID);

            getAllOrders();

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getAllOrders() {



            Query query = mOrderContent;
            options = new FirebaseRecyclerOptions.Builder<orderItemData>().setQuery(query, orderItemData.class).setLifecycleOwner(this).build();
            adapter = new FirebaseRecyclerAdapter<orderItemData, adminOrderPagerHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull adminOrderPagerHolder holder, int position, @NonNull final orderItemData model) {

                        if(model.getItemGms().equalsIgnoreCase("0")){
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemQuantity()+" "+ model.getItemUnit());
                        }else {
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemQuantity()+" "+ model.getItemUnit() + " , " + model.getItemGms() + " gms");
                        }if(model.getItemQuantity().equalsIgnoreCase("0")){
                            holder.txt_itemName.setText(model.getItemName()+" x "+model.getItemGms()+" "+ " gms");
                        }



                        holder.txt_itemPrice.setText("Rs. "+model.getItemFinalPrice());

                        Log.v("itemPrice",""+model.itemFinalPrice);



                }

                @NonNull
                @Override
                public adminOrderPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.admin_order_pager_layout, parent, false);

                    return new adminOrderPagerHolder(view);
                }
            };

            rl_item.setAdapter(adapter);



    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
