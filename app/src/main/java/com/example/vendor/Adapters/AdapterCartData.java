package com.example.vendor.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.CartData;
import com.example.vendor.R;

import com.example.vendor.UserScreens.CartFragment;
import com.example.vendor.UserScreens.EditCartItemFragment;
import com.example.vendor.UserScreens.HomeFragment;
import com.example.vendor.UserScreens.categoriesItemFragment;
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

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterCartData extends RecyclerView.Adapter<AdapterCartData.HolderCartItem> {

    private Context context;
    ArrayList<CartData> cartItems;

    AlertDialog.Builder builder;




    public AdapterCartData(Context context, ArrayList<CartData> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        builder = new AlertDialog.Builder(context);

    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_layout, parent, false);

        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderCartItem holder, final int position) {

        CartData cartData = cartItems.get(position);
        final String title = cartData.getItem_Name();
        final String itemPrice = cartData.getItem_Price();
        final String itemFinalCost = cartData.getItem_Final_Cost();
        final String itemQuantity = cartData.getItem_Quantity();
        final String ItemUnit = cartData.getItem_Unit();
        final String ItemImage = cartData.getItem_Image();
        final String ItemGms = cartData.getItem_Gms();



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
                        holder.txt_marathi_name.setText("Model downloaded");
                        firebaseTranslator.translate(title)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        holder.txt_marathi_name.setText(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        holder.txt_marathi_name.setText(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.txt_marathi_name.setText("Download failed");
                    }
                });


        holder.txtCartItem.setText("" + title);


        if(ItemUnit.equalsIgnoreCase("Kgs")){

            if(Double.parseDouble(itemQuantity)<=0){
                holder.txt_quantityItem.setVisibility(View.INVISIBLE);
                holder.txt_unit.setVisibility(View.INVISIBLE);
            }
            if(ItemGms.equalsIgnoreCase("0")){

                holder.txt_quantityItem.setVisibility(View.INVISIBLE);
                holder.txt_unit.setVisibility(View.INVISIBLE);

                holder.txt_quantity_gms.setText(""+itemQuantity);
                holder.txt_unit_gms.setText(""+ ItemUnit);
            }else {
                holder.txt_quantity_gms.setText(""+ItemGms);
                holder.txt_unit_gms.setText("Gms");

                holder.txt_quantityItem.setText(""+itemQuantity);
                holder.txt_unit.setText(""+ ItemUnit);
            }






        }else if(ItemUnit.equalsIgnoreCase("bunch")){
            holder.txt_quantity_gms.setVisibility(View.INVISIBLE);
            holder.txt_unit_gms.setVisibility(View.INVISIBLE);
            holder.txt_quantityItem.setText(""+itemQuantity);
            holder.txt_unit.setText(""+ ItemUnit);


        }else if (ItemUnit.equalsIgnoreCase("dozen")){
            holder.txt_quantity_gms.setVisibility(View.INVISIBLE);
            holder.txt_unit_gms.setVisibility(View.INVISIBLE);
            holder.txt_quantityItem.setText(""+itemQuantity);
            holder.txt_unit.setText(""+ ItemUnit);
        }



        try {
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
           picasso.load(ItemImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(holder.imageCartItem, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(ItemImage).placeholder(R.drawable.ic_cart_blue).into(holder.imageCartItem);
                }
            });
        } catch (Exception e) {
            holder.imageCartItem.setImageResource(R.drawable.ic_cart_blue);
        }


        holder.rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlEdit.getVisibility() == View.INVISIBLE) {
                    holder.rlEdit.setVisibility(View.VISIBLE);
                    holder.rlDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.rlEdit.setVisibility(View.INVISIBLE);
                    holder.rlDelete.setVisibility(View.INVISIBLE);
                }

            }
        });


        holder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(getApplicationContext(), mRef.toString(), Toast.LENGTH_SHORT).show();
                try {

//show dialog


                    builder.setTitle("Delete")
                            .setMessage("Are you sure, you want to delete the product " + title + "?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //delete
                                    EasyDB easyDB = EasyDB.init(context, "ITEM_DB")
                                            .setTableName("ITEMS_TABLE")
                                            .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                                            .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                                            .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                                            .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                                            .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                                            .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                                            .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                                            .doneTableColumn();

                                    boolean b = easyDB.deleteRow(1, title);

                                    if (b) {
                                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                                        cartItems.remove(position);
                                        notifyItemChanged(position);
                                        notifyDataSetChanged();

                                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                        FragmentTransaction transection= null;

                                        CartFragment ldf = new CartFragment ();
                                        transection =fragmentManager.beginTransaction();


                                        transection.replace(R.id.fragment_container, ldf);
                                        transection.commit();



                                    } else {
                                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                    }


                                    dialog.cancel();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancel
                            dialog.dismiss();
                        }
                    }).show();


                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });


        holder.rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();


                FragmentTransaction transection= null;

                    EditCartItemFragment ldf = new EditCartItemFragment ();
                    transection =manager.beginTransaction();

                Bundle bundle=new Bundle();
                bundle.putString("itemName",title);
                bundle.putString("itemQuantity",itemQuantity);
                bundle.putString("itemPriceEach",itemPrice);
                bundle.putString("itemFinalPrice",itemFinalCost);
                bundle.putString("itemUnit",ItemUnit);
                bundle.putString("itemImage",ItemImage);
                bundle.putString("itemGms",ItemGms);

                ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();











//                Intent intent = new Intent(context, EditCartItemActivity.class);
//                intent.putExtra("itemName", title);
//                intent.putExtra("itemQuantity", itemQuantity);
//                intent.putExtra("itemPriceEach", itemPrice);
//                intent.putExtra("itemFinalPrice", itemFinalCost);
//                intent.putExtra("itemUnit", ItemUnit);
//                intent.putExtra("itemImage", ItemImage);
//                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }


    class HolderCartItem extends RecyclerView.ViewHolder {

        public View view;

        private ImageView imageCartItem;
        public TextView txtCartItem, txt_quantityItem, txt_unit,  txt_quantity_gms, txt_unit_gms, txt_marathi_name;

        public RelativeLayout rlCart, rlDelete, rlEdit;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            imageCartItem = view.findViewById(R.id.imageCartItem);
            txtCartItem = view.findViewById(R.id.txt_itemCartName);
            txt_quantityItem = view.findViewById(R.id.txtQuantityCartKgs);
            rlCart = view.findViewById(R.id.relativeCart);
            rlDelete = view.findViewById(R.id.btn_cart_delete);
            rlEdit = view.findViewById(R.id.btn_cart_edit);
            txt_unit = view.findViewById(R.id.txt_cart_unitKgs);

            txt_quantity_gms = view.findViewById(R.id.txtQuantityCartGms);
            txt_unit_gms = view.findViewById(R.id.txt_cart_unitGms);

            txt_marathi_name = view.findViewById(R.id.txt_itemCartNameInMarathi);
        }
    }
}
