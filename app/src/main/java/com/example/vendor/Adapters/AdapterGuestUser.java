package com.example.vendor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.gusteContentData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import static android.content.Context.MODE_PRIVATE;

public class AdapterGuestUser extends RecyclerView.Adapter<AdapterGuestUser.HolderProductGuestUser> {


    /**
     * adapter for to display product
     */
    private Context context;
    public ArrayList<gusteContentData> productList;

    private double  finalCost = 0;


    DatabaseReference mCartRef;
    String userId;

    Activity activity;
    AlertDialog.Builder builder;


    public static final String[] ItemQuantity = {
            "Kgs",
            "Gms",
            "Pcs"
    };


    public AdapterGuestUser(Context context, ArrayList<gusteContentData> productList) {
        this.context = context;
        this.productList = productList;
        builder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public AdapterGuestUser.HolderProductGuestUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.guest_row_list, parent, false);

        SharedPreferences sh =context.getSharedPreferences("MyLogin",MODE_PRIVATE);
        userId = sh.getString("userId","");
        return new HolderProductGuestUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterGuestUser.HolderProductGuestUser holder, int position) {
        final gusteContentData modelProduct = productList.get(position);
        final String ItemNames = modelProduct.getItemName();
        final String ItemImages = modelProduct.getItemImage();
        final String itemPrice = modelProduct.getItemGuestPrice();
        final String itemUnit = modelProduct.getItemUnit();


        final int ItemPrice;

        if(!TextUtils.isEmpty(itemPrice) ){
            ItemPrice  = Integer.parseInt(itemPrice);
        }else {
           ItemPrice = 0;
        }



        final int[] quantity = {1};
        holder.ItemName.setText(""+ItemNames);
        holder.ItemQuantity.setText(""+ quantity[0]);
        String itemUnitData = modelProduct.getItemUnit();

        final String[] itemQuantityType = new String[1];
        holder.sec_unit.setText(ItemQuantity[0]);
        holder.btn_sec_quant_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Choose Quantity")
                        .setItems(ItemQuantity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemQuantityType[0] = ItemQuantity[which];
                                holder.sec_unit.setText(itemQuantityType[0]);


                            }
                        }).show();
            }
        });

        holder.ItemPrice.setText(""+itemPrice+" /Rs");
        try{
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(ItemImages).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                }
            });
        }catch (Exception e){
            holder.ItemImage.setImageResource(R.drawable.ic_cart_blue);
        }





        holder.btn_add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    quantity[0]++;

                    holder.ItemQuantity.setText(""+ quantity[0]);











            }
        });

        holder.btn_min_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quantity[0] >1){
                    quantity[0]--;

                    holder.ItemQuantity.setText(""+ quantity[0]);



                }

            }
        });


        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String   qu = holder.ItemQuantity.getText().toString();
                Toast.makeText(context, qu, Toast.LENGTH_SHORT).show();
              quantity[0] = Integer.parseInt(qu);


                finalCost = quantity[0] * ItemPrice;


                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                if(mAuth.getUid() != null){
                    mCartRef = FirebaseDatabase.getInstance().getReference().child("cartListener").child(FirebaseAuth.getInstance().getUid());
                }else if(mAuth.getUid() == null) {
                    mCartRef = FirebaseDatabase.getInstance().getReference().child("cartListener").child(userId);
                }

                String finalUnit = holder.sec_unit.getText().toString();

                String finalCosts = String.valueOf(finalCost);

                addToCart(ItemNames, itemPrice, finalCosts, qu, finalUnit, ItemImages);


                holder.ItemQuantity.setFocusable(false);
                holder.ItemQuantity.setEnabled(false);
                holder.ItemQuantity.setCursorVisible(false);
                holder.ItemQuantity.setKeyListener(null);
                holder.ItemQuantity.setBackgroundColor(Color.TRANSPARENT);

                holder.btn_add_to_cart.setClickable(false);

                holder.itemView.setBackgroundColor(Color.parseColor("#16000000"));
            }
        });





    }

    private void addToCart(String itemNames, String itemPrice, String finalCosts, String qu, String finalUnit, String itemImages) {

        try{

            EasyDB easyDB = EasyDB.init(context,"ITEM_GUEST_DB")
                    .setTableName("ITEMS_GUEST_TABLE")
                    .addColumn(new Column("Item_Name",new String[] {"text","unique"}))
                    .addColumn(new Column("Item_Price",new String[] {"text","not null"}))
                    .addColumn(new Column("Item_Final_Cost",new String[] {"text","not null"}))
                    .addColumn(new Column("Item_Quantity",new String[] {"text","not null"}))
                    .addColumn(new Column("Item_Unit",new String[] {"text","not null"}))
                    .addColumn(new Column("Item_Image",new String[] {"text","not null"}))
                    .doneTableColumn();

            easyDB.addData("Item_Name",itemNames)
                    .addData("Item_Price",itemPrice)
                    .addData("Item_Final_Cost",finalCosts)
                    .addData("Item_Quantity",qu)
                    .addData("Item_Unit",finalUnit)
                    .addData("Item_Image",itemImages)
                    .doneDataAdding();

            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Log.v("dataException",e.getMessage());
        }







    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class HolderProductGuestUser extends RecyclerView.ViewHolder {

        View view;
        public TextView ItemName, ItemPrice, sec_unit;
        public ImageView ItemImage, btn_add_quantity, btn_min_quantity, btn_sec_quant_unit;

        public LinearLayout linearCart, btn_add_to_cart;
        public EditText  ItemQuantity;


        public HolderProductGuestUser(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ItemImage = view.findViewById(R.id.img_guest_item);
            ItemName = view.findViewById(R.id.txt_guest_itemName);
            ItemQuantity = view.findViewById(R.id.txt_quantity_guest);
            btn_add_quantity = view.findViewById(R.id.btn_add_quantity_guest);
            btn_min_quantity = view.findViewById(R.id.btn_minus_quantity_guest);
            linearCart = view.findViewById(R.id.cart_guest);
            ItemPrice = view.findViewById(R.id.txt_itemGuestPrice);
            btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart_guest);

            sec_unit = view.findViewById(R.id.txt_quantity_unit_guest);
            btn_sec_quant_unit = view.findViewById(R.id.img_select_unit_guest);

        }

    }




    }
