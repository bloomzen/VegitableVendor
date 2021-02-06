package com.example.vendor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;

public class adminOrderPagerHolder extends RecyclerView.ViewHolder {

    public View v;
   public TextView txt_itemName, txt_itemPrice;




    public adminOrderPagerHolder(@NonNull View itemView) {
        super(itemView);
        v = itemView;
        txt_itemName= v.findViewById(R.id.itemName_quantity);
        txt_itemPrice = v.findViewById(R.id.item_price);
    }

}
