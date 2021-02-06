package com.example.vendor.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CartViewHolder extends RecyclerView.ViewHolder {

    //cartListener view holder for cartListener items
    private ImageView imageCartItem;
    public  TextView txtCartItem, txt_quantityItem, txt_unit;
   public View view;
   public RelativeLayout rlCart, rlDelete, rlEdit;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        imageCartItem = view.findViewById(R.id.imageCartItem);
        txtCartItem = view.findViewById(R.id.txt_itemCartName);
       // txt_quantityItem =view.findViewById(R.id.txtQuantityCart);
        rlCart = view.findViewById(R.id.relativeCart);
        rlDelete = view.findViewById(R.id.btn_cart_delete);
        rlEdit = view.findViewById(R.id.btn_cart_edit);
       // txt_unit = view.findViewById(R.id.txt_cart_unit);


    }

    public void setImageCartItem(final String img, final Context context){
        try{
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(imageCartItem, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(img).placeholder(R.drawable.ic_cart_blue).into(imageCartItem);
                }
            });
        }catch (Exception e){
            imageCartItem.setImageResource(R.drawable.ic_cart_blue);
        }

    }

}