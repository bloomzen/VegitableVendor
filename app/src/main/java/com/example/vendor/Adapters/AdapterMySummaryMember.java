package com.example.vendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.MyOrderData;
import com.example.vendor.R;

import java.util.ArrayList;

public class AdapterMySummaryMember extends RecyclerView.Adapter<AdapterMySummaryMember.MyHolder> {

    Context context;
    ArrayList<MyOrderData> cartItemsMyOrderSummary;

    public AdapterMySummaryMember(Context context, ArrayList<MyOrderData> cartItemsMyOrderSummary) {
        this.context = context;
        this.cartItemsMyOrderSummary = cartItemsMyOrderSummary;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_summary, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        MyOrderData summary = cartItemsMyOrderSummary.get(position);
        String title = summary.getItemName();
        String quantity = summary.getItemQuantity();
        String unit = summary.getItemUnit();
        String gms = summary.getItemGms();

        if(gms.equalsIgnoreCase("0")){
            holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit);
        }else {
            holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit + " , " + gms + " bunch");
        }if(quantity.equalsIgnoreCase("0")){
            holder.txt_itemDetail.setText(title+" x "+gms+" "+ " gms");
        }
    }

    @Override
    public int getItemCount() {
        return cartItemsMyOrderSummary.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView txt_itemDetail;
        public View view;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_itemDetail = view.findViewById(R.id.itemDetails);

        }
    }
}
