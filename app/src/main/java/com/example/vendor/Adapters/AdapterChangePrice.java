package com.example.vendor.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.AdminScreen.ChangePriceFragment;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.UserScreens.EditCartItemFragment;
import com.example.vendor.UserScreens.categoriesItemFragment;
import com.example.vendor.gusteContentData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterChangePrice extends RecyclerView.Adapter<AdapterChangePrice.HolderChangePrice>  {

    Context context;
    ArrayList<ContentData> pricelist;


    public AdapterChangePrice(Context context, ArrayList<ContentData> contentData) {
        this.context = context;
        this.pricelist = contentData;

    }

    @NonNull
    @Override
    public HolderChangePrice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.price_update_layout,parent,false);
        return new HolderChangePrice(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderChangePrice holder, int position) {
        final ContentData modelProduct = pricelist.get(position);
        final String itemName = modelProduct.getItemName();
        final String image = modelProduct.getItemImage();

       final String cat = modelProduct.getItemCategory();
       final String isKgs = modelProduct.getIsKgs();




        holder.text_price_update_list.setText(""+itemName);

        holder.btn_update_price_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();


                FragmentTransaction transection= null;

                ChangePriceFragment ldf = new ChangePriceFragment ();
                transection =manager.beginTransaction();


                Bundle bundle=new Bundle();
              //  bundle.putString("changePrice",price);
                bundle.putString("itemName",itemName);

                bundle.putString("itemCategory",cat);
                bundle.putString("isKgs",isKgs);
                ldf.setArguments(bundle);
                transection.replace(R.id.fragment_Admin_container, ldf);
                transection.addToBackStack(null);
                transection.commit();

                Log.v("cat",""+ cat);
            }
        });

        try {
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(holder.image_price_update_list, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(image).placeholder(R.drawable.ic_cart_blue).into(holder.image_price_update_list);
                }
            });
        } catch (Exception e) {
            holder.image_price_update_list.setImageResource(R.drawable.ic_cart_blue);
        }
    }

    @Override
    public int getItemCount() {
        return pricelist.size();
    }


    class HolderChangePrice extends RecyclerView.ViewHolder {
       public View view;
       public ImageView image_price_update_list;
      public   TextView text_price_update_list;
      public   Button btn_update_price_list;


        public HolderChangePrice(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            image_price_update_list = view.findViewById(R.id.image_price_update_list);
            text_price_update_list = view.findViewById(R.id.text_price_update_list);
            btn_update_price_list = view.findViewById(R.id.btn_update_price_list);
        }
    }
}
