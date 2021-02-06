package com.example.vendor.UserScreens;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditCartItemFragment extends Fragment {


    public EditCartItemFragment() {
        // Required empty public constructor
    }

    ImageView imgEditBack, img_item_edt, img_incre, img_dec, img_select_Unit;
    TextView txt_edit_name, cartItemNameEdit, txt_ItemUnit;

    EditText txt_cart_quant;

    RelativeLayout btn_proceed;
    int quantity;
    double finalCost = 0.0;
    int priceEach;

    DatabaseReference mRef;

    String userId, userType;


    public static final String[] ItemQuantity = {
            "0",
           "250",
            "500",
            "750"
    };

    LinearLayout linerGmsText, linearLayoutButton;

    String itemSelected;
    TextView txt_gms_edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_cart_item, container, false);
        try {
            imgEditBack = view.findViewById(R.id.btn_edit_back);
            img_item_edt = view.findViewById(R.id.img_item_edit);
            img_incre = view.findViewById(R.id.imageEdit_increment);
            img_dec = view.findViewById(R.id.imageEdit_decrement);
            txt_edit_name = view.findViewById(R.id.txt_itemEditName);
            //  txt_descriptionTxt = view.findViewById(R.id.descriptionText);
            cartItemNameEdit = view.findViewById(R.id.cartItemNameEdit);
            txt_cart_quant = view.findViewById(R.id.txt_cartQuantity);
            btn_proceed = view.findViewById(R.id.btn_Proceed);
            txt_gms_edit = view.findViewById(R.id.txt_gms_unit_edit);
            txt_ItemUnit = view.findViewById(R.id.txt_cart_quantity_unit);
            img_select_Unit = view.findViewById(R.id.img_select_unit_gms_cart_edit);

            linerGmsText = view.findViewById(R.id.linear_gms_unit_edit);
            linearLayoutButton = view.findViewById(R.id.linear_gms);

            SharedPreferences sh = this.getActivity().getSharedPreferences("MyLogin", MODE_PRIVATE);
            userId = sh.getString("userId", "");
            userType = sh.getString("userType", "");

            mRef = FirebaseDatabase.getInstance().getReference().child("cartListener");


            Bundle bundle = getArguments();
            final String itemName = bundle.getString("itemName");
            final String itemQuantity = bundle.getString("itemQuantity");
            final String itemPriceEach = bundle.getString("itemPriceEach");
            String itemFinalPrice = bundle.getString("itemFinalPrice");
            String itemUnit = bundle.getString("itemUnit");
            final String itemImage = bundle.getString("itemImage");
            final String itemGms = bundle.getString("itemGms");



            imgEditBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartFragment ldf = new CartFragment();


                    FragmentTransaction transection = getFragmentManager().beginTransaction();


                    transection.replace(R.id.fragment_container, ldf);
                    transection.commit();
                }
            });

            try {
                //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                Picasso picasso = Picasso.with(getContext());
                picasso.setIndicatorsEnabled(false);
                picasso.load(itemImage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(img_item_edt, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso picasso = Picasso.with(getContext());
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(itemImage).placeholder(R.drawable.ic_cart_blue).into(img_item_edt);
                    }
                });
            } catch (Exception e) {
               img_item_edt.setImageResource(R.drawable.ic_cart_blue);
            }
            if(!itemUnit.equalsIgnoreCase("Kgs")){
              linerGmsText.setVisibility(View.GONE);
              linearLayoutButton.setVisibility(View.GONE);
            }
            priceEach = Integer.parseInt(itemPriceEach);
            txt_edit_name.setText("" + itemName);
            cartItemNameEdit.setText("" + itemName);
            txt_cart_quant.setText("" + itemQuantity);
            txt_ItemUnit.setText("" + itemUnit);
            txt_gms_edit.setText(""+ itemGms);
            quantity = Integer.parseInt(itemQuantity);




            img_select_Unit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Choose Quantity")
                            .setItems(ItemQuantity, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemSelected = ItemQuantity[which];

                                    txt_gms_edit.setText(""+itemSelected);

                                }
                            }).show();
                }
            });

            btn_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try{
                        if (userType.equalsIgnoreCase("Gold") || userType.equalsIgnoreCase("Silver")
                        || userType.equalsIgnoreCase("Bronze") || userType.equalsIgnoreCase("Fix")) {
                            EasyDB easyDB = EasyDB.init(getContext(), "ITEM_DB")
                                    .setTableName("ITEMS_TABLE")
                                    .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                                    .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                                    .doneTableColumn();


                            int ItemQuantity = Integer.parseInt( txt_cart_quant.getText().toString());
                            double FinalGms = 0.0;
                            if(itemSelected == null && ItemQuantity == 0){
                                Toast.makeText(Vendor.getAppContext(), "Select a quantity", Toast.LENGTH_SHORT).show();
                            }else {
                                if(itemSelected == null){
                                    finalCost = ItemQuantity * priceEach;
                                    FinalGms  = ((double) priceEach/1000)* Integer.parseInt(itemGms);

                                    finalCost = finalCost + FinalGms;
                                    itemSelected = itemGms;

                                }else {
                                    finalCost = ItemQuantity * priceEach;
                                    FinalGms  = ((double) priceEach/1000)* Integer.parseInt(itemSelected);

                                    finalCost = finalCost + FinalGms;

                                }





                                Cursor res = easyDB.searchInColumn(1, itemName, 1);
                                if (res != null) {
                                    res.moveToFirst();
                                    String ID = res.getString(0);
                                    Log.v("ID", ID);
                                    int id = Integer.parseInt(ID);

                                    try {
                                        easyDB.updateData(1, itemName)
                                                .updateData(2, itemPriceEach)
                                                .updateData(3, Double.toString(finalCost))
                                                .updateData(4, txt_cart_quant.getText().toString())
                                                .updateData(5, txt_ItemUnit.getText().toString())
                                                .updateData(6, itemImage)
                                                .updateData(7,itemSelected)
                                                .rowID(id)
                                        ;

                                        Toast.makeText(Vendor.getAppContext(), "updated", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.v("error", e.getMessage());
                                    }


                                } else {
                                    Toast.makeText(Vendor.getAppContext(), "error", Toast.LENGTH_SHORT).show();
                                }

                                CartFragment ldf = new CartFragment();


                                FragmentTransaction transection = getFragmentManager().beginTransaction();


                                transection.replace(R.id.fragment_container, ldf);
                                transection.commit();
                            }

                        } else if (userType.equalsIgnoreCase("guest")) {
                            EasyDB easyDB = EasyDB.init(getContext(), "ITEM_GUEST_DB")
                                    .setTableName("ITEMS_GUEST_TABLE")
                                    .addColumn(new Column("Item_Name", new String[]{"text", "unique"}))
                                    .addColumn(new Column("Item_Price", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Final_Cost", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Quantity", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Unit", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Image", new String[]{"text", "not null"}))
                                    .addColumn(new Column("Item_Gms", new String[]{"text", "not null"}))
                                    .doneTableColumn();


                            int ItemQuantity = Integer.parseInt( txt_cart_quant.getText().toString());

                            double FinalGms;

                            if(itemSelected == null && ItemQuantity == 0){
                                Toast.makeText(Vendor.getAppContext(), "Select a quantity", Toast.LENGTH_SHORT).show();
                            }else {
                                if(itemSelected == null){
                                    finalCost = ItemQuantity * priceEach;
                                    FinalGms  = ((double) priceEach/1000)* Integer.parseInt(itemGms);

                                    finalCost = finalCost + FinalGms;
                                    itemSelected = itemGms;

                                }else {
                                    finalCost = ItemQuantity * priceEach;
                                    FinalGms  = ((double) priceEach/1000)* Integer.parseInt(itemSelected);

                                    finalCost = finalCost + FinalGms;

                                }


                                Cursor res = easyDB.searchInColumn(1, itemName, 1);
                                if (res != null) {
                                    res.moveToFirst();
                                    String ID = res.getString(0);
                                    Log.v("ID", ID);
                                    int id = Integer.parseInt(ID);

                                    try {
                                        easyDB.updateData(1, itemName)
                                                .updateData(2, itemPriceEach)
                                                .updateData(3, Double.toString(finalCost) )
                                                .updateData(4, txt_cart_quant.getText().toString())
                                                .updateData(5, txt_ItemUnit.getText().toString())
                                                .updateData(6, itemImage)
                                                .updateData(7,itemSelected)
                                                .rowID(id)
                                        ;

                                        Toast.makeText(Vendor.getAppContext(), "updated", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.v("error", e.getMessage());
                                    }


                                } else {
                                    Toast.makeText(Vendor.getAppContext(), "error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            CartFragment ldf = new CartFragment();


                            FragmentTransaction transection = getFragmentManager().beginTransaction();


                            transection.replace(R.id.fragment_container, ldf);
                            transection.commit();


                        }


                    }catch (Exception e){
                        Toast.makeText(Vendor.getAppContext(), "On Data Change: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            });

            img_dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(quantity >0){
                        quantity = Integer.parseInt(txt_cart_quant.getText().toString());
                        quantity--;
                        txt_cart_quant.setText("" + quantity);
                    }





                }
            });

            img_incre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    quantity++;

                    txt_cart_quant.setText("" + quantity);


                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create View: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




        return view;
    }

}
