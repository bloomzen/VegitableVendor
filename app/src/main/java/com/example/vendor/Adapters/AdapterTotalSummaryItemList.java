package com.example.vendor.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.TotalSummaryConstructor;
import com.example.vendor.Vendor;
import com.example.vendor.orderItemData;

import java.util.ArrayList;

public class AdapterTotalSummaryItemList extends RecyclerView.Adapter<AdapterTotalSummaryItemList.HolderTotalSummary> {

    Context context;
 public    ArrayList<TotalSummaryConstructor> list;

    public AdapterTotalSummaryItemList(Context context, ArrayList<TotalSummaryConstructor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterTotalSummaryItemList.HolderTotalSummary onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_summary,parent,false);

        return new HolderTotalSummary(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTotalSummary holder, int position) {
        final TotalSummaryConstructor modelProduct = list.get(position);
        String itemName = modelProduct.getItemName();
        String itemQuantity = modelProduct.getItemQuantity();
        String itemUnit = modelProduct.getItemUnit();



        holder.txt_name_quantity.setText(itemName+" x "+itemQuantity+itemUnit);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderTotalSummary extends RecyclerView.ViewHolder{

        public View view;
        public TextView txt_name_quantity;

        public HolderTotalSummary(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_name_quantity = view.findViewById(R.id.itemDetails);
        }
    }
}
