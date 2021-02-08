package com.example.vendor.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.CartData;
import com.example.vendor.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class AdapterCartSummary extends RecyclerView.Adapter<AdapterCartSummary.CartItemSummaryViewHolder> {


    Context context;
    ArrayList<CartData> cartItemList;

    String userId,userType;


    public AdapterCartSummary(Context context){
        this.context = context;
    }


    public AdapterCartSummary(Context context, ArrayList<CartData> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartItemSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_summary, parent, false);

        return new CartItemSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemSummaryViewHolder holder, int position) {


        SharedPreferences sh = this.context.getSharedPreferences("MyLogin", MODE_PRIVATE);
        userId = sh.getString("userId", "");
        userType = sh.getString("userType", "");

        CartData cartData = cartItemList.get(position);
        String title = cartData.getItem_Name();
        String quantity = cartData.getItem_Quantity();
        String unit = cartData.getItem_Unit();
        String gms = cartData.getItem_Gms();
        String price = cartData.getItem_Final_Cost();






        if(userType.equalsIgnoreCase("guest")){
            if(gms.equalsIgnoreCase("0")){
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit+ " = " + price);
            }else {
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit + " , " + gms + " gms" + " = " + price);
            }if(quantity.equalsIgnoreCase("0")){
                holder.txt_itemDetail.setText(title+" x "+gms+" "+ " gms" + " = " + price);
            }if (unit.equalsIgnoreCase("dozen")){
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit + " = " + price);
            }
        }else if(userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver")
        || userType.equalsIgnoreCase("Bronze") || userType.equalsIgnoreCase("Fix")){
            if(gms.equalsIgnoreCase("0")){
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit);
            }else {
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit + " , " + gms + " gms" );
            }if(quantity.equalsIgnoreCase("0")){
                holder.txt_itemDetail.setText(title+" x "+gms+" "+ " gms" );
            }if (unit.equalsIgnoreCase("dozen")){
                holder.txt_itemDetail.setText(title+" x "+quantity+" "+ unit);
            }
        }





    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }


    public class CartItemSummaryViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_itemDetail;
        public View view;


        public CartItemSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_itemDetail = view.findViewById(R.id.itemDetails);

        }
    }

}


