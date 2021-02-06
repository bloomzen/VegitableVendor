package com.example.vendor.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateRemoteModel;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import static android.content.Context.MODE_PRIVATE;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.HolderProductUser> {


    /**
     * adapter for to display product
     */
    private Context context;
    public ArrayList<ContentData> productList;

    private double finalCost = 0;
    String userId, userType;
    String itemPrice;
    String itemUnit;


    boolean flag = false;


    int ItemPrice = 0;
    DatabaseReference mCartRef;

    AlertDialog.Builder builder;

    public static final String[] ItemQuantity = {
            "0",
            "250",
            "500",
            "750"
    };

    String itemGmsUnit = "0";

    int priceEach = 0;

    String ItemImages = "";

    public AdapterProductUser(Context context, ArrayList<ContentData> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public AdapterProductUser.HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_list, parent, false);

        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterProductUser.HolderProductUser holder, int position) {
        SharedPreferences sh = this.context.getSharedPreferences("MyLogin", MODE_PRIVATE);
        userId = sh.getString("userId", "");
        userType = sh.getString("userType", "");
        Log.v("user", "" + userType);
        final ContentData modelProduct = productList.get(position);
        final String ItemNames = modelProduct.getItemName();
        ItemImages = modelProduct.getItemImage();
        String isKgs = modelProduct.getIsKgs();


        final int[] quantity = {1};
        try {
            if (userType.equalsIgnoreCase("Gold")) {
                itemPrice = modelProduct.getItemPriceInGold();
                holder.itemPrice.setVisibility(View.GONE);
                Log.v("gold", "" + itemPrice);

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

                Cursor res = easyDB.searchInColumn(1, ItemNames, 1);
                if (res != null) {
                    res.moveToFirst();
                    String ID = res.getString(0);
                    Log.v("ID", ID);

                    String kgQuantity = res.getString(4);
                    String gmsQuantity = res.getString(7);
                    String unit = res.getString(5);

                    Toast.makeText(Vendor.getAppContext(), "" + unit, Toast.LENGTH_SHORT).show();
                    Log.v("goldSilver", "" + unit + kgQuantity + gmsQuantity);

                    if (unit.equalsIgnoreCase("Kgs")) {
                        if (kgQuantity.equalsIgnoreCase("0")) {
                            quantity[0] = 0;
                            itemGmsUnit = gmsQuantity;
                        } else {
                            quantity[0] = Integer.parseInt(kgQuantity);
                            itemGmsUnit = gmsQuantity;

                        }

                    } else {
                        quantity[0] = Integer.parseInt(kgQuantity);
                    }


                    holder.txt_add_to_cart.setText("Already in Cart");


                }

            } else if (userType.equalsIgnoreCase("Silver")) {
                itemPrice = modelProduct.getItemPriceInSilver();
                holder.itemPrice.setVisibility(View.GONE);
                Log.v("silver", "" + itemPrice);

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

                Cursor res = easyDB.searchInColumn(1, ItemNames, 1);
                if (res != null) {
                    res.moveToFirst();
                    String ID = res.getString(0);
                    Log.v("ID", ID);

                    String kgQuantity = res.getString(4);
                    String gmsQuantity = res.getString(7);

                    String unit = res.getString(5);

                    if (unit.equalsIgnoreCase("Kgs")) {
                        if (kgQuantity.equalsIgnoreCase("0")) {
                            quantity[0] = 0;
                            itemGmsUnit = gmsQuantity;
                        } else {
                            quantity[0] = Integer.parseInt(kgQuantity);
                            itemGmsUnit = gmsQuantity;

                        }

                    } else {
                        quantity[0] = Integer.parseInt(kgQuantity);
                    }

                    holder.txt_add_to_cart.setText("Already in Cart");


                }
            } else if (userType.equalsIgnoreCase("Bronze")) {
                itemPrice = modelProduct.getItemPriceInBronze();
                holder.itemPrice.setVisibility(View.GONE);
                Log.v("bronze", "" + itemPrice);
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

                Cursor res = easyDB.searchInColumn(1, ItemNames, 1);
                if (res != null) {
                    res.moveToFirst();
                    String ID = res.getString(0);
                    Log.v("ID", ID);

                    String kgQuantity = res.getString(4);
                    String gmsQuantity = res.getString(7);
                    String unit = res.getString(5);

                    if (unit.equalsIgnoreCase("Kgs")) {
                        if (kgQuantity.equalsIgnoreCase("0")) {
                            quantity[0] = 0;
                            itemGmsUnit = gmsQuantity;
                        } else {
                            quantity[0] = Integer.parseInt(kgQuantity);
                            itemGmsUnit = gmsQuantity;

                        }

                    } else {
                        quantity[0] = Integer.parseInt(kgQuantity);
                    }

                    holder.txt_add_to_cart.setText("Already in Cart");


                }
            } else if (userType.equalsIgnoreCase("Fix")) {
                itemPrice = modelProduct.getItemPriceInFix();
                holder.itemPrice.setVisibility(View.GONE);
                Log.v("fix", "" + itemPrice);

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

                Cursor res = easyDB.searchInColumn(1, ItemNames, 1);
                if (res != null) {
                    res.moveToFirst();
                    String ID = res.getString(0);
                    Log.v("ID", ID);

                    String kgQuantity = res.getString(4);
                    String gmsQuantity = res.getString(7);

                    String unit = res.getString(5);

                    if (unit.equalsIgnoreCase("Kgs")) {
                        if (kgQuantity.equalsIgnoreCase("0")) {
                            quantity[0] = 0;
                            itemGmsUnit = gmsQuantity;
                        } else {
                            quantity[0] = Integer.parseInt(kgQuantity);
                            itemGmsUnit = gmsQuantity;

                        }

                    } else {
                        quantity[0] = Integer.parseInt(kgQuantity);
                    }

                    holder.txt_add_to_cart.setText("Already in Cart");


                }
            } else if (userType.equalsIgnoreCase("Guest")) {
                itemPrice = modelProduct.getItemPriceInGuest();
                holder.itemPrice.setText("Rs " + itemPrice + "/kg");
                Log.v("guest", "" + itemPrice);

                EasyDB easyDB = EasyDB.init(context, "ITEM_GUEST_DB")
                        .setTableName("ITEMS_GUEST_TABLE")
                        .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                        .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                        .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                        .doneTableColumn();

                Cursor res = easyDB.searchInColumn(1, ItemNames, 1);
                if (res != null) {
                    res.moveToFirst();
                    String ID = res.getString(0);
                    Log.v("ID", ID);

                    String kgQuantity = res.getString(4);
                    String gmsQuantity = res.getString(7);

                    String unit = res.getString(5);
                    Toast.makeText(Vendor.getAppContext(), "" + kgQuantity, Toast.LENGTH_SHORT).show();


                    if (unit.equalsIgnoreCase("Kgs")) {
                        if (kgQuantity.equalsIgnoreCase("0")) {
                            quantity[0] = 0;
                            itemGmsUnit = gmsQuantity;
                        } else {
                            quantity[0] = Integer.parseInt(kgQuantity);
                            itemGmsUnit = gmsQuantity   ;

                        }

                    } else {
                        quantity[0] = Integer.parseInt(kgQuantity);
                    }

                    holder.txt_add_to_cart.setText("Already in Cart");


                }
            }

            if (isKgs.equals("true")) {
                holder.txt_item_quantity.setText("Kgs");
            } else if (isKgs.equals("false")) {
                holder.txt_item_quantity.setText("bunch");
                holder.linearLayout.setVisibility(View.GONE);
                holder.linearGmsUnit.setVisibility(View.GONE);

            }else if(isKgs.equals("dozen")){
                holder.txt_item_quantity.setText("dozen");
                holder.linearLayout.setVisibility(View.GONE);
                holder.linearGmsUnit.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.v("excep", "" + e.getMessage());
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
                        holder.itemNameInMarathi.setText("Model downloaded");
                        firebaseTranslator.translate(modelProduct.getItemName())
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        holder.itemNameInMarathi.setText(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        holder.itemNameInMarathi.setText(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.itemNameInMarathi.setText("Download failed");
                    }
                });
        holder.ItemName.setText("" + ItemNames);
        holder.ItemQuantity.setText("" + quantity[0]);
        holder.txt_gms_unit.setText(""+itemGmsUnit);



        holder.ItemSecQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Grams")
                        .setItems(ItemQuantity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemGmsUnit = ItemQuantity[which];
                                holder.txt_gms_unit.setText(itemGmsUnit);
                                //
                                //  holder.txt_item_quantity.setText(itemGmsUnit);


                            }
                        }).show();
            }
        });

        try {
            //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(modelProduct.getItemImage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(modelProduct.getItemImage()).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                }
            });
        } catch (Exception e) {
            holder.ItemImage.setImageResource(R.drawable.ic_cart_blue);
        }


        holder.btn_add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                quantity[0] = Integer.parseInt(holder.ItemQuantity.getText().toString());
                quantity[0]++;

                holder.ItemQuantity.setText("" + quantity[0]);


            }
        });

        holder.btn_min_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity[0] > 0) {
                    quantity[0] = Integer.parseInt(holder.ItemQuantity.getText().toString());
                    quantity[0]--;

                    holder.ItemQuantity.setText("" + quantity[0]);


                }

            }
        });


        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver") || userType.equalsIgnoreCase("Bronze")
                        || userType.equalsIgnoreCase("Fix")) {
                    try {
                        String qu = holder.ItemQuantity.getText().toString();
                        // Toast.makeText(context, qu, Toast.LENGTH_SHORT).show();
                        quantity[0] = Integer.parseInt(qu);

                        int itemGms;
                        if (itemGmsUnit == null) {
                            itemGms = 0;
                        } else {
                            itemGms = Integer.parseInt(itemGmsUnit);
                        }

                        if (quantity[0] < 1 && itemGms == 0) {
                            Toast.makeText(Vendor.getAppContext(), "Select a Quantity", Toast.LENGTH_SHORT).show();
                        } else {
                            if (holder.txt_add_to_cart.getText().toString().equalsIgnoreCase("Already in Cart")) {

                                Toast.makeText(Vendor.getAppContext(), "Already in cart", Toast.LENGTH_SHORT).show();
                            } else {
                                if (userType.equalsIgnoreCase("Gold")) {
                                    priceEach = Integer.parseInt(modelProduct.getItemPriceInGold());
                                } else if (userType.equalsIgnoreCase("Silver")) {
                                    priceEach = Integer.parseInt(modelProduct.getItemPriceInSilver());
                                } else if (userType.equalsIgnoreCase("Bronze")) {
                                    priceEach = Integer.parseInt(modelProduct.getItemPriceInBronze());
                                } else if (userType.equalsIgnoreCase("Fix")) {
                                    priceEach = Integer.parseInt(modelProduct.getItemPriceInFix());
                                }


                                ItemPrice = priceEach;
                                String finalUnit = holder.txt_item_quantity.getText().toString();
                                finalCost = quantity[0] * ItemPrice;


                                Log.v("unit", "unit: " + itemGmsUnit);
                                // Toast.makeText(Vendor.getAppContext(), "gms: " + itemGmsUnit, Toast.LENGTH_SHORT).show();

                                Log.v("itemGms", "gms: " + Integer.toString(itemGms));
                                //Toast.makeText(Vendor.getAppContext(), "" + Integer.toString(itemGms), Toast.LENGTH_SHORT).show();

                                Log.v("item Price", "price: " + ItemPrice);
                                double FinalGms = ((double) ItemPrice / 1000) * itemGms;

                                Log.v("finalGms", "final gms: " + FinalGms);
                                finalCost = finalCost + FinalGms;


                                Log.v("finalCosts", "final Cost: " + Double.toString(finalCost));
                                // Toast.makeText(Vendor.getAppContext(), "" + Double.toString(finalCost), Toast.LENGTH_SHORT).show();

                                String finalCosts = String.valueOf(finalCost);
                                addToCart(ItemNames, Integer.toString(priceEach), finalCosts, qu, finalUnit, modelProduct.getItemImage(), Integer.toString(itemGms));
                            }

                        }


                    } catch (Exception e) {
                        Log.v("exce", "" + e.getMessage());

                    }


                } else if (userType.equalsIgnoreCase("Guest")) {
                    try {
                        String qu = holder.ItemQuantity.getText().toString();
                        Toast.makeText(context, qu, Toast.LENGTH_SHORT).show();
                        quantity[0] = Integer.parseInt(qu);
                        int itemGms;
                        if (itemGmsUnit == null) {
                            itemGms = 0;
                        } else {
                            itemGms = Integer.parseInt(itemGmsUnit);
                        }


                        if (quantity[0] < 1 && itemGms == 0) {

                            Toast.makeText(Vendor.getAppContext(), "Select a quantity", Toast.LENGTH_SHORT).show();

                        } else {
                            if (holder.txt_add_to_cart.getText().toString().equalsIgnoreCase("Already in Cart")) {
                                Toast.makeText(Vendor.getAppContext(), "Already in Cart", Toast.LENGTH_SHORT).show();
                            } else {
                                ItemPrice = Integer.parseInt(modelProduct.getItemPriceInGuest());
                                String finalUnit = holder.txt_item_quantity.getText().toString();
                                finalCost = quantity[0] * ItemPrice;
                                Log.v("itemsPrice", "item price: " + ItemPrice);
                                Log.v("quantity", "quants" + quantity[0]);


                                Log.v("unit", "unit: " + itemGmsUnit);
                                // Toast.makeText(Vendor.getAppContext(), "gms: " + itemGmsUnit, Toast.LENGTH_SHORT).show();

                                Log.v("itemGms", "gms: " + Integer.toString(itemGms));
                                //  Toast.makeText(Vendor.getAppContext(), "" + Integer.toString(itemGms), Toast.LENGTH_SHORT).show();

                                Log.v("item Price", "price: " + ItemPrice);
                                double FinalGms = ((double) ItemPrice / 1000) * itemGms;

                                Log.v("finalGms", "final gms: " + FinalGms);
                                finalCost = finalCost + FinalGms;


                                Log.v("finalCosts", "final Cost: " + Double.toString(finalCost));
                                Toast.makeText(Vendor.getAppContext(), "" + Double.toString(finalCost), Toast.LENGTH_SHORT).show();

                                String finalCosts = String.valueOf(finalCost);
                                addToCartGuest(ItemNames, modelProduct.getItemPriceInGuest(), finalCosts, qu, finalUnit, modelProduct.getItemImage(), Integer.toString(itemGms));
                                holder.txt_add_to_cart.setText("Already in Cart");

                            }


                        }


                    } catch (Exception e) {
                        Log.v("exce", "" + e.getMessage());

                    }
                }


            }
        });


    }


    private void addToCartGuest(String itemNames, String itemPrice, String finalCosts, String qu, String finalUnit, String itemImages, String itemGms) {

        try {

            EasyDB easyDB = EasyDB.init(context, "ITEM_GUEST_DB")
                    .setTableName("ITEMS_GUEST_TABLE")
                    .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                    .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                    .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                    .doneTableColumn();

            easyDB.addData("Item_Name", itemNames)
                    .addData("Item_Price", itemPrice)
                    .addData("Item_Final_Cost", finalCosts)
                    .addData("Item_Quantity", qu)
                    .addData("Item_Unit", finalUnit)
                    .addData("Item_Image", itemImages)
                    .addData("Item_Gms", itemGms)
                    .doneDataAdding();

            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.v("dataException", e.getMessage());
        }


    }

    private void addToCart(String itemNames, String itemPrice, String finalCosts, String qu, String finalUnit, String itemImages, String itemGms) {
        try {

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

            easyDB.addData("Item_Name", itemNames)
                    .addData("Item_Price", itemPrice)
                    .addData("Item_Final_Cost", finalCosts)
                    .addData("Item_Quantity", qu)
                    .addData("Item_Unit", finalUnit)
                    .addData("Item_Image", itemImages)
                    .addData("Item_Gms", itemGms)

                    .doneDataAdding();

            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.v("dataException", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class HolderProductUser extends RecyclerView.ViewHolder {

        View view;
        public TextView ItemName, txt_item_quantity, itemNameInMarathi, itemPrice, txt_gms_unit, txt_add_to_cart;
        public ImageView ItemImage, btn_add_quantity, btn_min_quantity, ItemSecQuantity;
        public Button btn_add;
        public LinearLayout linearCart, btn_add_to_cart;
        public EditText ItemQuantity;
        LinearLayout linearLayout, linearGmsUnit;


        public HolderProductUser(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txt_add_to_cart = view.findViewById(R.id.txt_add_to_cart);
            ItemImage = view.findViewById(R.id.img_item);
            ItemName = view.findViewById(R.id.txt_itemName);
            ItemQuantity = view.findViewById(R.id.txt_quantity);
            btn_add_quantity = view.findViewById(R.id.btn_add_quantity);
            btn_min_quantity = view.findViewById(R.id.btn_minus_quantity);
            txt_item_quantity = view.findViewById(R.id.txt_quantity_unit);
            ItemSecQuantity = view.findViewById(R.id.img_select_unit_gms);
            itemNameInMarathi = view.findViewById(R.id.txt_item_name_marathi);
            linearLayout = view.findViewById(R.id.linear_gms);
            itemPrice = view.findViewById(R.id.txt_itemGuestPrice);
            txt_gms_unit = view.findViewById(R.id.txt_gms_unit);

            linearGmsUnit = view.findViewById(R.id.linear_gms_unit);
            linearCart = view.findViewById(R.id.cart);


            btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart);

        }

    }


}
