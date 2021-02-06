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
import com.example.vendor.AdminScreen.TotalHotelItem;

import com.example.vendor.R;
import com.example.vendor.UserScreens.CartFragment;
import com.example.vendor.UserScreens.categoriesItemFragment;

import java.util.ArrayList;

public class AdapterHotelList extends RecyclerView.Adapter<AdapterHotelList.HolderHotelNameList> {

    Context context;
    ArrayList<AdminHotelOrder> adminHotelOrders;
    String dateTime;

    public AdapterHotelList(Context context, ArrayList<AdminHotelOrder> adminHotelOrders, String dateTime) {
        this.context = context;
        this.adminHotelOrders = adminHotelOrders;
        this.dateTime = dateTime;
    }

    @NonNull
    @Override
    public HolderHotelNameList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.hotel_list, parent, false);

        return new HolderHotelNameList(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHotelNameList holder, int position) {
        AdminHotelOrder adminHotelOrder = adminHotelOrders.get(position);
        final String name = adminHotelOrder.getHotelName();

        holder.txt_HotelName.setText("" + name);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalHotelItem ldf = new TotalHotelItem();

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transection = null;


                transection = fragmentManager.beginTransaction();


                Bundle bundle = new Bundle();
                bundle.putString("dateTime",dateTime);
                bundle.putString("hotelName",name);

                 ldf.setArguments(bundle);
                transection.replace(R.id.fragment_Admin_container, ldf);
                transection.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminHotelOrders.size();
    }


    public class HolderHotelNameList extends RecyclerView.ViewHolder {

        public View view;
        public TextView txt_HotelName;


        public HolderHotelNameList(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_HotelName = view.findViewById(R.id.catHotelName);


        }

    }
}
