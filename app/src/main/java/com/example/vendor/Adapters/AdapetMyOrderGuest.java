package com.example.vendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.MyOrderData;
import com.example.vendor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapetMyOrderGuest extends RecyclerView.Adapter<AdapetMyOrderGuest.myHolderMember> {

    Context context;
    ArrayList<MyOrderData> cartItemsMyOrder;

    public AdapetMyOrderGuest(Context context, ArrayList<MyOrderData> cartItemsMyOrder) {
        this.context = context;
        this.cartItemsMyOrder = cartItemsMyOrder;
    }

    @NonNull
    @Override
    public myHolderMember onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_layout_guest, parent, false);

        return new myHolderMember(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolderMember holder, int position) {

        MyOrderData cartData = cartItemsMyOrder.get(position);
        final String itemName = cartData.getItemName();
        final String itemImage = cartData.getItemImage();
        String itemFinalPrice = cartData.getItemFinalPrice();
        String itemGms = cartData.getItemGms();
        String itemQuantity = cartData.getItemQuantity();
        String itemUnit = cartData.getItemUnit();

        holder.txt_itemPrice.setText("Rs "+itemFinalPrice);
        try {
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(itemImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(holder.imageCartItem, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(itemImage).placeholder(R.drawable.ic_cart_blue).into(holder.imageCartItem);
                }
            });
        } catch (Exception e) {
            holder.imageCartItem.setImageResource(R.drawable.ic_cart_blue);
        }

        holder.txtCartItem.setText(""+itemName);

        if(itemUnit.equalsIgnoreCase("Kgs")){

            if(Double.parseDouble(itemQuantity)<=0){
                holder.txt_quantityItem.setVisibility(View.INVISIBLE);
                holder.txt_unit.setVisibility(View.INVISIBLE);
            }
            if(itemGms.equalsIgnoreCase("0")){



                holder.txt_quant_Gms.setVisibility(View.INVISIBLE);
                holder.txt_unit_gms.setVisibility(View.INVISIBLE);

                holder.txt_quantityItem.setText(""+itemQuantity);
                holder.txt_unit.setText(""+ itemUnit);
            }else {
                holder.txt_quant_Gms.setText(""+itemGms);
                holder.txt_unit_gms.setText("Gms");

                holder.txt_quantityItem.setText(""+itemQuantity);
                holder.txt_unit.setText(""+ itemUnit);
            }



        }else if(itemUnit.equalsIgnoreCase("bunch") || itemUnit.equalsIgnoreCase("dozen")){
            holder.txt_quant_Gms.setVisibility(View.INVISIBLE);
            holder.txt_unit_gms.setVisibility(View.INVISIBLE);
            holder.txt_quantityItem.setText(""+itemQuantity);
            holder.txt_unit.setText(""+ itemUnit);
        }


        // Create an English-German translator:

        FirebaseTranslatorOptions firebaseTranslatorOptions = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                .setTargetLanguage(FirebaseTranslateLanguage.MR)
                .build();

        final FirebaseTranslator firebaseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(firebaseTranslatorOptions);

        FirebaseModelDownloadConditions firebaseModelDownloadConditions = new FirebaseModelDownloadConditions.Builder().build();

        firebaseTranslator.downloadModelIfNeeded(firebaseModelDownloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.txt_itemCartNameInMarathiGuest.setText("Model downloaded");
                        firebaseTranslator.translate(itemName)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        holder.txt_itemCartNameInMarathiGuest.setText(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        holder.txt_itemCartNameInMarathiGuest.setText(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.txt_itemCartNameInMarathiGuest.setText("Download failed");
                    }
                });


    }

    @Override
    public int getItemCount() {
        return cartItemsMyOrder.size();
    }

    public class myHolderMember extends RecyclerView.ViewHolder{
        private ImageView imageCartItem;
        public TextView txtCartItem, txt_quantityItem, txt_unit, txt_itemPrice, txt_quant_Gms, txt_unit_gms, txt_itemCartNameInMarathiGuest;
        public View view;
        public RelativeLayout rlCart, rlDelete, rlEdit;

        public myHolderMember(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageCartItem = view.findViewById(R.id.imageCartItemGuest);
            txtCartItem = view.findViewById(R.id.txt_itemCartNameGuest);
            txt_quantityItem =view.findViewById(R.id.txtQuantityCartGuestKgs);
            rlCart = view.findViewById(R.id.relativeCartGuest);
            rlDelete = view.findViewById(R.id.btn_cart_deleteGuest);
            rlEdit = view.findViewById(R.id.btn_cart_edit_Guest);
            txt_unit = view.findViewById(R.id.txt_cart_unitGuestKgs);
            txt_itemPrice = view.findViewById(R.id.txt_cart_priceGuest);

            txt_quant_Gms = view.findViewById(R.id.txtQuantityCartGuestGms);
            txt_unit_gms = view.findViewById(R.id.txt_cart_unitGuestGms);

            txt_itemCartNameInMarathiGuest = view.findViewById(R.id.txt_itemCartNameInMarathiGuest);
        }
    }
}
