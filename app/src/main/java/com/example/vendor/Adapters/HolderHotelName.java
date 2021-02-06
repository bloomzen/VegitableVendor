package com.example.vendor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;

public class HolderHotelName extends RecyclerView.ViewHolder {

    public View view;
    public TextView txt_HotelName;




    public HolderHotelName(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        txt_HotelName = view.findViewById(R.id.catHotelName);


    }

}
