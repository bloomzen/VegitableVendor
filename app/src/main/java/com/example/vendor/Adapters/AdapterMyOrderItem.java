package com.example.vendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.CartData;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.orderItemData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdapterMyOrderItem extends RecyclerView.Adapter<AdapterMyOrderItem.HolderMyOrderItem>{


    /**
     * adapter for myorder in user side
     */
    Context context;
    ArrayList<orderItemData> listMyOrder;

    public AdapterMyOrderItem(Context context, ArrayList<orderItemData> listMyOrder) {
        this.context = context;
        this.listMyOrder = listMyOrder;
    }

    @NonNull
    @Override
    public HolderMyOrderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);

        return new HolderMyOrderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMyOrderItem holder, int position) {
        final orderItemData modelProduct = listMyOrder.get(position);

        String itemName = modelProduct.getItemName();
        String itemQuantity = modelProduct.getItemQuantity();
        String itemUnit = modelProduct.getItemUnit();
        String itemGms = modelProduct.getItemGms();

        if(Integer.parseInt(itemGms) > 0){
            holder.itemQuantity.setText(""+itemQuantity + itemUnit +" , " +itemGms + "Gms");
        }else {
            holder.itemQuantity.setText(""+itemQuantity + itemUnit);
        }



        holder.itemName.setText(""+itemName);


    }

    @Override
    public int getItemCount() {
        return listMyOrder.size();
    }


    public class HolderMyOrderItem extends RecyclerView.ViewHolder{

        public TextView itemName, itemQuantity, itemUnit;

        public HolderMyOrderItem(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txt_item_name);
            itemQuantity = itemView.findViewById(R.id.txt_item_quantity);

        }
    }
}
