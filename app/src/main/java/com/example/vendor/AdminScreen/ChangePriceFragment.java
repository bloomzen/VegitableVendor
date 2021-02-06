package com.example.vendor.AdminScreen;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.UserScreens.HomeFragment;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePriceFragment extends Fragment {


    public ChangePriceFragment() {
        // Required empty public constructor
    }

    DatabaseReference db_chnage_price;

    Button btn_cat_gold, btn_cat_silver, btn_cat_bronnze, btn_cat_fix, btn_cat_guest;
    TextView txt_currentPrice, txt_name_in_marathi;
    EditText edt_change_priceGold, edt_change_item_name, edt_change_priceSilver, edt_change_priceBronze, edt_change_priceFix, edt_change_priceGuest;
    RelativeLayout rl_price;

    String gold_price = "0", silver_price = "0", bronze_price = "0", fix_price = "0", guest_price = "0";

    String itemCategory, itemPrice, itemName, isKgs, storeName;

    ProgressDialog mProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_price, container, false);

        try {
            btn_cat_gold = view.findViewById(R.id.btn_cat_gold);
            btn_cat_silver = view.findViewById(R.id.btn_cat_silver);
            btn_cat_bronnze = view.findViewById(R.id.btn_cat_bronze);
            btn_cat_fix = view.findViewById(R.id.btn_cat_fixed);
            btn_cat_guest = view.findViewById(R.id.btn_cat_guest);
            txt_currentPrice = view.findViewById(R.id.txt_currentPrice);
            edt_change_priceGold = view.findViewById(R.id.edt_changePriceGold);
            edt_change_priceGuest = view.findViewById(R.id.edt_changePriceGuest);
            edt_change_priceSilver = view.findViewById(R.id.edt_changePriceSilver);
            edt_change_priceBronze = view.findViewById(R.id.edt_changePriceBronze);
            edt_change_priceFix = view.findViewById(R.id.edt_changePriceFixed);
            rl_price = view.findViewById(R.id.btn_price_change);
            txt_name_in_marathi = view.findViewById(R.id.txt_name_in_marathi);
            edt_change_item_name = view.findViewById(R.id.txt_itemName_change_price);

            Bundle bundle = getArguments();

            itemName = bundle.getString("itemName");
            itemPrice = bundle.getString("changePrice");
            itemCategory = bundle.getString("itemCategory");
            isKgs = bundle.getString("isKgs");


            storeName = itemName;

            //Toast.makeText(Vendor.getAppContext(), ""+itemName+itemCategory, Toast.LENGTH_SHORT).show();

            db_chnage_price = FirebaseDatabase.getInstance().getReference().child("mart");


            mProgress = new ProgressDialog(getContext());
            mProgress.setTitle("Please Wait");
            mProgress.setMessage("updating the prices");

            edt_change_item_name.setText("" + itemName);
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
                            txt_name_in_marathi.setText("Model downloaded");
                            firebaseTranslator.translate(itemName)
                                    .addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            txt_name_in_marathi.setText(s);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            txt_name_in_marathi.setText(e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            txt_name_in_marathi.setText("Download failed");
                        }
                    });


            change_gold_price();


            edt_change_item_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    itemName = charSequence.toString();
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
                                    txt_name_in_marathi.setText("Model downloaded");
                                    firebaseTranslator.translate(itemName)
                                            .addOnSuccessListener(new OnSuccessListener<String>() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    txt_name_in_marathi.setText(s);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    txt_name_in_marathi.setText(e.getMessage());
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    txt_name_in_marathi.setText("Download failed");
                                }
                            });

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            edt_change_priceGold.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    gold_price = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            edt_change_priceGuest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    guest_price = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edt_change_priceFix.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    fix_price = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edt_change_priceBronze.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    bronze_price = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            edt_change_priceSilver.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    silver_price = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            btn_cat_guest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chnage_guest_price();
                }
            });

            btn_cat_fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    change_fixed_price();
                }
            });

            btn_cat_bronnze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    change_bronze_price();
                }
            });

            btn_cat_silver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    change_silver_price();
                }
            });

            btn_cat_gold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    change_gold_price();
                }
            });


            rl_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgress.show();

                    if (edt_change_item_name.getText().toString().isEmpty()) {
                        mProgress.dismiss();
                        Toast.makeText(Vendor.getAppContext(), "Item name is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(Vendor.getAppContext(), ""+storeName, Toast.LENGTH_SHORT).show();


                    db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String key = "";
                            if (snapshot.hasChildren()) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    key = ds.getKey();
                                }

                                final String finalKey = key;

                                db_chnage_price.child(itemCategory).child(key).child("itemName").setValue(edt_change_item_name.getText().toString());


                                if (!gold_price.equals("0")) {
                                    db_chnage_price.child(itemCategory).child(key).child("itemPriceInGold").setValue(gold_price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgress.dismiss();
                                            gold_price = "0";
                                        }
                                    });
                                }


                                if (!silver_price.equals("0")) {
                                    db_chnage_price.child(itemCategory).child(key).child("itemPriceInSilver").setValue(silver_price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgress.dismiss();

                                            silver_price = "0";
                                        }
                                    });
                                }


                                if (!bronze_price.equals("0")) {
                                    db_chnage_price.child(itemCategory).child(key).child("itemPriceInBronze").setValue(bronze_price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgress.dismiss();
                                            bronze_price = "0";
                                        }
                                    });
                                }




                                if (!fix_price.equals("0")) {
                                    db_chnage_price.child(itemCategory).child(key).child("itemPriceInFix").setValue(fix_price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgress.dismiss();
                                            fix_price = "0";
                                        }
                                    });
                                }


                                if (!guest_price.equals("0")) {
                                    db_chnage_price.child(itemCategory).child(key).child("itemPriceInGuest").setValue(guest_price).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgress.dismiss();
                                            Toast.makeText(Vendor.getAppContext(), "Price Updated", Toast.LENGTH_SHORT).show();


                                            guest_price = "0";
                                        }
                                    });
                                }


                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = null;
                                if (fragmentManager != null) {
                                    mProgress.dismiss();
                                    transaction = fragmentManager.beginTransaction();
                                   // transaction.setReorderingAllowed(true);

                                    // Replace whatever is in the fragment_container view with this fragment
                                    transaction.replace(R.id.fragment_Admin_container, new UpdatePriceListFragment(), null);

                                    // Commit the transaction
                                    transaction.commit();

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Change Price: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void change_gold_price() {

        btn_cat_gold.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_cat_gold.setTextColor(Color.WHITE);

        btn_cat_bronnze.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_bronnze.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_guest.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_silver.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_fix.setTextColor(getResources().getColor(R.color.textColorHint));

        if (gold_price != null) {
            edt_change_priceGold.setText("" + gold_price);
        }
        Log.v("item", "" + itemCategory + itemName);


        db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.v("key", "" + key);
                    }

                    db_chnage_price.child(itemCategory).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String price = snapshot.child("itemPriceInGold").getValue().toString();
                            Log.v("price", "" + price);
                            Toast.makeText(Vendor.getAppContext(), "" + price, Toast.LENGTH_SHORT).show();
                            txt_currentPrice.setText("" + price + " RS");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edt_change_priceFix.setVisibility(View.INVISIBLE);
        edt_change_priceBronze.setVisibility(View.INVISIBLE);
        edt_change_priceGuest.setVisibility(View.INVISIBLE);
        edt_change_priceSilver.setVisibility(View.INVISIBLE);
        edt_change_priceGold.setVisibility(View.VISIBLE);


    }


    private void change_silver_price() {

        btn_cat_silver.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_cat_silver.setTextColor(Color.WHITE);

        btn_cat_bronnze.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_bronnze.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_gold.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_guest.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_fix.setTextColor(getResources().getColor(R.color.textColorHint));
        if (silver_price != null) {
            edt_change_priceSilver.setText("" + silver_price);
        }
        db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.v("key", "" + key);
                    }

                    db_chnage_price.child(itemCategory).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String price = snapshot.child("itemPriceInSilver").getValue().toString();
                            Log.v("price", "" + price);
                            Toast.makeText(Vendor.getAppContext(), "" + price, Toast.LENGTH_SHORT).show();
                            txt_currentPrice.setText("" + price + " RS");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edt_change_priceGold.setVisibility(View.INVISIBLE);
        edt_change_priceBronze.setVisibility(View.INVISIBLE);
        edt_change_priceGuest.setVisibility(View.INVISIBLE);
        edt_change_priceFix.setVisibility(View.INVISIBLE);
        edt_change_priceSilver.setVisibility(View.VISIBLE);
    }


    private void change_bronze_price() {

        btn_cat_bronnze.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_cat_bronnze.setTextColor(Color.WHITE);

        btn_cat_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_guest.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_gold.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_silver.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_fix.setTextColor(getResources().getColor(R.color.textColorHint));

        if (bronze_price != null) {
            edt_change_priceBronze.setText("" + bronze_price);
        }

        db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.v("key", "" + key);
                    }

                    db_chnage_price.child(itemCategory).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String price = snapshot.child("itemPriceInBronze").getValue().toString();
                            Log.v("price", "" + price);
                            Toast.makeText(Vendor.getAppContext(), "" + price, Toast.LENGTH_SHORT).show();
                            txt_currentPrice.setText("" + price + " RS");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edt_change_priceGold.setVisibility(View.INVISIBLE);
        edt_change_priceFix.setVisibility(View.INVISIBLE);
        edt_change_priceGuest.setVisibility(View.INVISIBLE);
        edt_change_priceSilver.setVisibility(View.INVISIBLE);
        edt_change_priceBronze.setVisibility(View.VISIBLE);
    }

    private void change_fixed_price() {

        btn_cat_fix.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_cat_fix.setTextColor(Color.WHITE);

        btn_cat_bronnze.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_bronnze.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_gold.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_silver.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_guest.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_guest.setTextColor(getResources().getColor(R.color.textColorHint));

        if (fix_price != null) {
            edt_change_priceFix.setText("" + fix_price);
        }

        db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.v("key", "" + key);
                    }

                    db_chnage_price.child(itemCategory).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String price = snapshot.child("itemPriceInFix").getValue().toString();
                            Log.v("price", "" + price);
                            Toast.makeText(Vendor.getAppContext(), "" + price, Toast.LENGTH_SHORT).show();
                            txt_currentPrice.setText("" + price + " RS");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edt_change_priceGold.setVisibility(View.INVISIBLE);
        edt_change_priceBronze.setVisibility(View.INVISIBLE);
        edt_change_priceGuest.setVisibility(View.INVISIBLE);
        edt_change_priceSilver.setVisibility(View.INVISIBLE);
        edt_change_priceFix.setVisibility(View.VISIBLE);
    }

    private void chnage_guest_price() {

        btn_cat_guest.setBackgroundColor(getResources().getColor(R.color.textColorHint));
        btn_cat_guest.setTextColor(Color.WHITE);

        btn_cat_bronnze.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_bronnze.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_gold.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_gold.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_silver.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_silver.setTextColor(getResources().getColor(R.color.textColorHint));

        btn_cat_fix.setBackgroundColor(getResources().getColor(R.color.cart_item));
        btn_cat_fix.setTextColor(getResources().getColor(R.color.textColorHint));
        if (guest_price != null) {
            edt_change_priceGuest.setText("" + guest_price);
        }

        db_chnage_price.child(itemCategory).orderByChild("itemName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = "";
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        key = ds.getKey();
                        Log.v("key", "" + key);
                    }

                    db_chnage_price.child(itemCategory).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String price = snapshot.child("itemPriceInGuest").getValue().toString();
                            Log.v("price", "" + price);
                            Toast.makeText(Vendor.getAppContext(), "" + price, Toast.LENGTH_SHORT).show();
                            txt_currentPrice.setText("" + price + " RS");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edt_change_priceGold.setVisibility(View.INVISIBLE);
        edt_change_priceBronze.setVisibility(View.INVISIBLE);
        edt_change_priceFix.setVisibility(View.INVISIBLE);
        edt_change_priceSilver.setVisibility(View.INVISIBLE);
        edt_change_priceGuest.setVisibility(View.VISIBLE);

    }

}
