package com.example.vendor.AdminScreen;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.UserScreens.cartListener;
import com.example.vendor.UserScreens.categoriesItemFragment;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddItemsFragment extends Fragment {

    DatabaseReference dbItem;

    public AdminAddItemsFragment() {
        // Required empty public constructor
    }

    String[] quantityType;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    public static final String[] ItemQuantity = {
            "Kgs",
            "dozen",
            "bunch"
    };

    public static final String[] IteCategory = {
            "leafy",
            "Vegetable",
            "Fruit",
            "Exotic"
    };

    String priceGold, priceSilver, priceBronze, priceGuest, priceFix, priceGmsGold, priceGmsSilver, priceGmsBronze, priceGmsGuest, priceGmsFix, pricebunchGold, pricebunchSilver, pricebunchBronze, pricebunchGuest, pricebunchFix;


    CircleImageView circleImageView;
    EditText edt_name_english, edt_name_hindi, edt_price;
    TextView txt_quantity, txt_category;
    ImageView img_quntity, img_category;
    RelativeLayout rl_add;

    String itemNameInEnglish, itemPrice, itemCategory;

    static int Gallery_pick = 1;
    Bitmap thumb_bitmap;

    byte[] thumb_bite = {};
    Context context;

    String imageUrl = null
            ;
    StorageReference mStoragePath;

    ValueEventListener valueEventListener;

    ProgressDialog mProgress;

    String itemQuantityType  = "";

    EditText edt_price_kgs_gold, edt_price_gms_gold, edt_price_pcs_gold, edt_price_kgs_silver, edt_price_gms_silver, edt_price_pcs_silver;
    EditText edt_price_kgs_bronze, edt_price_gms_bronze, edt_price_pcs_bronze, edt_price_kgs_fix, edt_price_gms_fix, edt_price_pcs_fix;
    EditText edt_price_kgs_guest, edt_price_gms_guest, edt_price_pcs_guest;

    Button btn_gold, btn_silver, btn_bronze, btn_fix, btn_guest;

    String itemInKgs, itemInGms, itemInbunch;
    int counter = 0;

    cartListener cartListener1;


    int count = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context != null && context instanceof cartListener) {
            cartListener1 = (cartListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add_items, container, false);

        circleImageView = view.findViewById(R.id.img_add_items);
        edt_name_english = view.findViewById(R.id.edt_addItemNameInEnglish);


        txt_quantity = view.findViewById(R.id.txt_addItemQuantity);
        txt_category = view.findViewById(R.id.txt_addItemCategory);
        img_quntity = view.findViewById(R.id.img_add_quantity_type);
        img_category = view.findViewById(R.id.img_add_category);
        rl_add = view.findViewById(R.id.btn_add_item);

        edt_price_kgs_gold = view.findViewById(R.id.edt_addItemPriceInKgsGold);
        edt_price_kgs_silver = view.findViewById(R.id.edt_addItemPriceInKgsSilver);
        edt_price_kgs_bronze = view.findViewById(R.id.edt_addItemPriceInKgsBronze);
        edt_price_kgs_fix = view.findViewById(R.id.edt_addItemPriceInKgsFix);
        edt_price_kgs_guest = view.findViewById(R.id.edt_addItemPriceInKgsGuest);


        edt_price_pcs_gold = view.findViewById(R.id.edt_addItemPriceInPcsGold);
        edt_price_pcs_silver = view.findViewById(R.id.edt_addItemPriceInPcsSilver);
        edt_price_pcs_bronze = view.findViewById(R.id.edt_addItemPriceInPcsBronze);
        edt_price_pcs_fix = view.findViewById(R.id.edt_addItemPriceInPcsFix);
        edt_price_pcs_guest = view.findViewById(R.id.edt_addItemPriceInPcsGuest);

        btn_gold = view.findViewById(R.id.btn_cat_gold_add_Items);
        btn_silver = view.findViewById(R.id.btn_cat_silver_add_Items);
        btn_bronze = view.findViewById(R.id.btn_cat_bronze_add_Items);
        btn_fix = view.findViewById(R.id.btn_cat_fixed_add_Items);
        btn_guest = view.findViewById(R.id.btn_cat_guest_add_Items);

        quantityType = getResources().getStringArray(R.array.quantity_type);
        checkedItems = new boolean[ItemQuantity.length];


        mProgress = new ProgressDialog(getActivity());
        mProgress.setTitle("Please wait");
        mProgress.setCanceledOnTouchOutside(false);


        mStoragePath = FirebaseStorage.getInstance().getReference();

        dbItem = FirebaseDatabase.getInstance().getReference().child("mart");




        edt_price_kgs_gold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceGold = charSequence.toString();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_kgs_guest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceGuest = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_kgs_fix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceFix = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edt_price_kgs_bronze.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceBronze = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_kgs_silver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceSilver = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        edt_price_pcs_gold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceGold = charSequence.toString();
                Log.v("gold",""+pricebunchGold);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_pcs_silver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceSilver = charSequence.toString();
                Log.v("gold",""+pricebunchSilver);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_pcs_bronze.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceBronze = charSequence.toString();
                Log.v("gold",""+pricebunchBronze);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_pcs_guest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceGuest = charSequence.toString();
                Log.v("gold",""+pricebunchGuest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_price_pcs_fix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                priceFix = charSequence.toString();
                Log.v("gold",""+pricebunchFix);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        itemQuantityType = ItemQuantity[0];

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);



                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
            }
        });


        img_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Category")
                        .setItems(IteCategory, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemCategory = IteCategory[which];
                                txt_category.setText(itemCategory);
                                txt_category.setTextColor(Color.BLACK);

                            }
                        }).show();
            }
        });


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.hasChildren()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        count =(int)( count + ds.getChildrenCount());
                        Log.v("count",""+count);

                    }


                }









            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbItem.addListenerForSingleValueEvent(valueEventListener);




        img_quntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Quantity")
                        .setItems(ItemQuantity, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        itemQuantityType = ItemQuantity[i];
                                        txt_quantity.setText(itemQuantityType);
                                        txt_quantity.setTextColor(Color.BLACK);

                                       changeGold();
                                    }
                                });






                builder.show();
            }
        });





        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });




        btn_gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeGold();

            }
        });

        btn_silver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_silver.setBackgroundColor(getResources().getColor(R.color.textColorHint));
                btn_silver.setTextColor(Color.WHITE);

                btn_bronze.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_bronze.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_guest.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_gold.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_fix.setTextColor(getResources().getColor(R.color.textColorHint));

                edt_price_kgs_silver.setVisibility(View.VISIBLE);
                edt_price_kgs_gold.setVisibility(View.GONE);
                edt_price_kgs_bronze.setVisibility(View.GONE);
                edt_price_kgs_guest.setVisibility(View.GONE);
                edt_price_kgs_fix.setVisibility(View.GONE);

                edt_price_pcs_silver.setVisibility(View.VISIBLE);
                edt_price_pcs_gold.setVisibility(View.GONE);
                edt_price_pcs_bronze.setVisibility(View.GONE);
                edt_price_pcs_guest.setVisibility(View.GONE);
                edt_price_pcs_fix.setVisibility(View.GONE);


               if(itemQuantityType == "Kgs"){
                   edt_price_pcs_silver.setVisibility(View.GONE);
               }else if(itemQuantityType == "bunch"){
                   edt_price_kgs_silver.setVisibility(View.GONE);
                   edt_price_pcs_silver.setHint("enter price in bunch");
               }else if (itemQuantityType == "dozen"){
                   edt_price_kgs_silver.setVisibility(View.GONE);
                   edt_price_pcs_silver.setHint("enter price in dozen");
               }


            }
        });

        btn_bronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_bronze.setBackgroundColor(getResources().getColor(R.color.textColorHint));
                btn_bronze.setTextColor(Color.WHITE);

                btn_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_gold.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_guest.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_silver.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_fix.setTextColor(getResources().getColor(R.color.textColorHint));

                edt_price_kgs_bronze.setVisibility(View.VISIBLE);
                edt_price_kgs_silver.setVisibility(View.GONE);
                edt_price_kgs_gold.setVisibility(View.GONE);
                edt_price_kgs_guest.setVisibility(View.GONE);
                edt_price_kgs_fix.setVisibility(View.GONE);


                edt_price_pcs_bronze.setVisibility(View.VISIBLE);
                edt_price_pcs_silver.setVisibility(View.GONE);
                edt_price_pcs_gold.setVisibility(View.GONE);
                edt_price_pcs_guest.setVisibility(View.GONE);
                edt_price_pcs_fix.setVisibility(View.GONE);


                if(itemQuantityType == "Kgs"){
                    edt_price_pcs_bronze.setVisibility(View.GONE);
                }else if(itemQuantityType == "bunch"){
                    edt_price_kgs_bronze.setVisibility(View.GONE);
                    edt_price_pcs_bronze.setHint("enter price in bunch");
                }else if(itemQuantityType == "dozen"){
                    edt_price_kgs_bronze.setVisibility(View.GONE);
                    edt_price_pcs_bronze.setHint("enter price in dozen");
                }


            }
        });

        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_guest.setBackgroundColor(getResources().getColor(R.color.textColorHint));
                btn_guest.setTextColor(Color.WHITE);

                btn_bronze.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_bronze.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_gold.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_silver.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_fix.setTextColor(getResources().getColor(R.color.textColorHint));

                edt_price_kgs_guest.setVisibility(View.VISIBLE);
                edt_price_kgs_silver.setVisibility(View.GONE);
                edt_price_kgs_bronze.setVisibility(View.GONE);
                edt_price_kgs_fix.setVisibility(View.GONE);
                edt_price_kgs_gold.setVisibility(View.GONE);


                edt_price_pcs_guest.setVisibility(View.VISIBLE);
                edt_price_pcs_silver.setVisibility(View.GONE);
                edt_price_pcs_bronze.setVisibility(View.GONE);
                edt_price_pcs_fix.setVisibility(View.GONE);
                edt_price_pcs_gold.setVisibility(View.GONE);

                if(itemQuantityType == "Kgs"){
                    edt_price_pcs_guest.setVisibility(View.GONE);
                }else if(itemQuantityType == "bunch"){
                    edt_price_kgs_guest.setVisibility(View.GONE);
                    edt_price_pcs_guest.setHint("enter price in bunch");
                }else if(itemQuantityType == "dozen"){
                    edt_price_kgs_guest.setVisibility(View.GONE);
                    edt_price_pcs_guest.setHint("enter price in dozen");
                }


            }
        });

        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_fix.setBackgroundColor(getResources().getColor(R.color.textColorHint));
                btn_fix.setTextColor(Color.WHITE);

                btn_bronze.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_bronze.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_guest.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_silver.setTextColor(getResources().getColor(R.color.textColorHint));

                btn_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
                btn_gold.setTextColor(getResources().getColor(R.color.textColorHint));

                edt_price_kgs_fix.setVisibility(View.VISIBLE);
                edt_price_kgs_silver.setVisibility(View.GONE);
                edt_price_kgs_bronze.setVisibility(View.GONE);
                edt_price_kgs_guest.setVisibility(View.GONE);
                edt_price_kgs_gold.setVisibility(View.GONE);



                edt_price_pcs_fix.setVisibility(View.VISIBLE);
                edt_price_pcs_silver.setVisibility(View.GONE);
                edt_price_pcs_bronze.setVisibility(View.GONE);
                edt_price_pcs_guest.setVisibility(View.GONE);
                edt_price_pcs_gold.setVisibility(View.GONE);

                if(itemQuantityType == "Kgs"){
                    edt_price_pcs_fix.setVisibility(View.GONE);
                }else if(itemQuantityType == "bunch"){
                    edt_price_pcs_fix.setHint("enter price in bunch");
                    edt_price_kgs_fix.setVisibility(View.GONE);
                }else if(itemQuantityType == "dozen"){
                    edt_price_kgs_fix.setVisibility(View.GONE);
                    edt_price_pcs_fix.setHint("enter price in dozen");
                }


            }
        });


        return view;
    }


    private void changeGold() {

        btn_gold.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_gold.setTextColor(Color.WHITE);

        btn_bronze.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_bronze.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_guest.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_silver.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_fix.setTextColor(getResources().getColor(R.color.textColorHint));

        edt_price_kgs_gold.setVisibility(View.VISIBLE);
        edt_price_kgs_silver.setVisibility(View.GONE);
        edt_price_kgs_bronze.setVisibility(View.GONE);
        edt_price_kgs_guest.setVisibility(View.GONE);
        edt_price_kgs_fix.setVisibility(View.GONE);



        edt_price_pcs_gold.setVisibility(View.VISIBLE);
        edt_price_pcs_silver.setVisibility(View.GONE);
        edt_price_pcs_bronze.setVisibility(View.GONE);
        edt_price_pcs_guest.setVisibility(View.GONE);
        edt_price_pcs_fix.setVisibility(View.GONE);


        if(itemQuantityType.equalsIgnoreCase("Kgs") ){
            edt_price_pcs_gold.setVisibility(View.GONE);
        }else if(itemQuantityType.equalsIgnoreCase("bunch")  ){
            edt_price_kgs_gold.setVisibility(View.GONE);
            edt_price_pcs_gold.setHint("enter price in bunch");
        }else if(itemQuantityType.equalsIgnoreCase("dozen")){
            edt_price_kgs_gold.setVisibility(View.GONE);
            edt_price_pcs_gold.setHint("enter price in dozen");
        }




    }


    private void CheckData() {

        mProgress.setMessage("Checking credentials......");
        mProgress.show();



        itemNameInEnglish = edt_name_english.getText().toString().trim();


        if (itemNameInEnglish == null  && itemCategory == null && itemQuantityType == null) {
            mProgress.hide();
            Toast.makeText(Vendor.getAppContext(), "empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        addItem();


    }

    private void addItem() {




        counter = count + 1;


        try {
            mProgress.setMessage("Saving Item info..");
            HashMap<String, Object> addData = new HashMap<>();
            String isKgs = "";

            if(itemQuantityType.equals("Kgs")){
                isKgs = "true";
            }else if(itemQuantityType.equals("bunch")){
                isKgs = "false";
            }

            String cate = "";

            if(itemCategory.equalsIgnoreCase("leafy")){
                cate = "2LeafyVegetable";
            }else if(itemCategory.equalsIgnoreCase("Vegetable")){
                cate = "1IndianVegetable";
            }else if(itemCategory.equalsIgnoreCase("Exotic")){
                cate = "3Exotic";
            }else if(itemCategory.equalsIgnoreCase("Fruit")){
                cate = "4Fruit";
            }

            addData.put("itemName", itemNameInEnglish);

            addData.put("itemUnit", itemQuantityType);
            addData.put("itemPriceInGold",priceGold);
            addData.put("itemPriceInSilver",priceSilver);
            addData.put("itemPriceInBronze",priceBronze);
            addData.put("itemPriceInGuest",priceGuest);
            addData.put("itemPriceInFix",priceFix);


            addData.put("itemCategory",cate);
            addData.put("productId",Integer.toString(counter));

            //Log.v("items",""+priceKgsBronze);




            final StorageReference thumb_filepath = mStoragePath.child("/" + cate).child(itemNameInEnglish + ".jpg");

            final String finalCate = cate;
            dbItem.child(cate).child(Integer.toString(counter)).updateChildren(addData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    try {
                        UploadTask uploadTask = thumb_filepath.putBytes(thumb_bite);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                thumb_filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        imageUrl = task.getResult().toString();
                                        Map images = new HashMap<>();

                                        images.put("itemImage", imageUrl);
                                        dbItem.child(finalCate).child(Integer.toString(counter)).child("itemImage").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    mProgress.dismiss();
                                                    if (cartListener1 != null) {
                                                        cartListener1.navListener("cart");
                                                    }

                                                    Toast.makeText(Vendor.getAppContext(), "item Updated", Toast.LENGTH_SHORT).show();
                                                    circleImageView.setImageResource(R.drawable.ic_cart_blue);
                                                    edt_name_english.setText("");

                                                    edt_price_kgs_bronze.setText("");
                                                    edt_price_kgs_silver.setText("");
                                                    edt_price_kgs_gold.setText("");
                                                    edt_price_kgs_fix.setText("");
                                                    edt_price_kgs_guest.setText("");

                                                    edt_price_pcs_bronze.setText("");
                                                    edt_price_pcs_gold.setText("");
                                                    edt_price_pcs_silver.setText("");
                                                    edt_price_pcs_guest.setText("");
                                                    edt_price_pcs_fix.setText("");

                                                    itemInKgs = "";
                                                    itemInbunch = "";
                                                    txt_category.setText("Add category");
                                                    txt_category.setTextColor(getResources().getColor(R.color.textColorHint));
                                                    txt_quantity.setText("Add quantity Type");
                                                    txt_quantity.setTextColor(getResources().getColor(R.color.textColorHint));

                                                    AdminHotelListFragment ldf = new AdminHotelListFragment ();

                                                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                                                    transection.replace(R.id.fragment_Admin_container, ldf);

                                                    transection.commit();
                                                } else {

                                                    mProgress.hide();
                                                    Toast.makeText(Vendor.getAppContext(), "error images", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgress.hide();
                                Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        mProgress.hide();
                        Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.hide();
                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Admin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }






    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == Gallery_pick && resultCode == RESULT_OK) {

                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);


            }
            //Toast.makeText(getContext(),String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) , Toast.LENGTH_SHORT).show();
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {


                    Uri resultUri = result.getUri();

                    File thum_file = new File(resultUri.getPath());

                    //Toast.makeText(getContext(), "cropImage2", Toast.LENGTH_SHORT).show();


                    thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(getContext()).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(thum_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_bite = baos.toByteArray();

                    Picasso.with(getContext()).load(thum_file).placeholder(R.drawable.ic_cart_blue).into(circleImageView);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}