package com.example.vendor.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.AdminHotelOrder;
import com.example.vendor.AdminScreen.AdminViewHotelOrderFragment;
import com.example.vendor.R;

import java.util.ArrayList;

public class AdapterCategoryOrders extends RecyclerView.Adapter<AdapterCategoryOrders.HolderCategoryOrders> {

    Context context;
  public   ArrayList<AdminHotelOrder> list;
    String member, hotelName;

    public AdapterCategoryOrders(Context context, ArrayList<AdminHotelOrder> adminHotelOrders) {
        this.context = context;
        this.list = adminHotelOrders;
    }

    @NonNull
    @Override
    public HolderCategoryOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list, parent, false);

        return new HolderCategoryOrders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategoryOrders holder, int position) {
        final AdminHotelOrder adminHotelOrder = list.get(position);
        holder.txt_hotelNumber.setText("Order ID:" + "" + adminHotelOrder.getOrderNumber());
        holder.txt_hotelTime.setText("Order ON:" + "" + adminHotelOrder.getOrderDate() + "\n" + adminHotelOrder.getOrderTime());
        holder.txt_Hotel_Name.setText("HotelName: "+ adminHotelOrder.getHotelName());

        member = adminHotelOrder.getMemberType();
        hotelName = adminHotelOrder.getHotelName();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminViewHotelOrderFragment hotelOrderFragment = new AdminViewHotelOrderFragment();

                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();

                FragmentTransaction transection = null;
                transection = manager.beginTransaction();


                Bundle bundle = new Bundle();
                bundle.putString("hotelName", hotelName);
                bundle.putString("orderID", adminHotelOrder.getOrderNumber());
                bundle.putString("orderON", adminHotelOrder.getOrderDate() + "\n" + adminHotelOrder.getOrderTime());
                bundle.putString("member", member);
                bundle.putString("status", adminHotelOrder.getOrderStatus());
                bundle.putString("total",adminHotelOrder.getTotal_order_amount());
                hotelOrderFragment.setArguments(bundle);

                transection.replace(R.id.fragment_Admin_container, hotelOrderFragment).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class HolderCategoryOrders extends RecyclerView.ViewHolder {

        public View view;
        public TextView txt_hotelNumber, txt_hotelTime, txt_Hotel_Name;

        public HolderCategoryOrders(@NonNull View itemView) {
            super(itemView);
            view = itemView;


            txt_hotelNumber = view.findViewById(R.id.txt_Hotel_orderNumber);
            txt_hotelTime = view.findViewById(R.id.txt_Hotel_orderDate);
            txt_Hotel_Name = view.findViewById(R.id.txt_Hotel_Name);


        }
    }
}
