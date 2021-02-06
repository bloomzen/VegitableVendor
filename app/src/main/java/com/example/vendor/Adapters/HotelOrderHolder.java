package com.example.vendor.Adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;

public class HotelOrderHolder extends RecyclerView.ViewHolder {

   public View view;
  public TextView txt_hotelNumber, txt_hotelTime;
 public   Button btn_status;

    public HotelOrderHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;


        txt_hotelNumber = view.findViewById(R.id.txt_Hotel_orderNumber);
        txt_hotelTime = view.findViewById(R.id.txt_Hotel_orderDate);
       // btn_status = view.findViewById(R.id.btn_orderStatus);


    }
}
