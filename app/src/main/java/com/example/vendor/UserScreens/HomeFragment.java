package com.example.vendor.UserScreens;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterGuestUser;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.ContentData;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.gusteContentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * home fragment to show all data
     */

    View view;
    Button btn_allProducts, btn_vege, btn_fruits, btn_exo_vege, btn_leafy_vegetables;
    DatabaseReference dbRef;
    RecyclerView recyclerViewGroceries;

    ArrayList<ContentData> productList;
    AdapterProductUser adapterProductUser;

    ArrayList<gusteContentData> productGuestList;
    AdapterGuestUser adapterGuestUser;

    String itemName, itemPic, itemPrice, itemQuantity, itemImage;
    FirebaseAuth mAuth;

    ProgressDialog mProgress;

    String userType, userId;

    ImageView img_banner;
    ScrollView dataScroll;

    NestedScrollView nestedScrollView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            btn_allProducts = view.findViewById(R.id.btn_allProducts);
            btn_vege = view.findViewById(R.id.btn_vegetables);
            btn_fruits = view.findViewById(R.id.btn_fruits);
            btn_exo_vege = view.findViewById(R.id.btn_exotic_fruits);
            btn_leafy_vegetables = view.findViewById(R.id.btn_leafyvegetables);

            img_banner = view.findViewById(R.id.image_banner);

            nestedScrollView = view.findViewById(R.id.nested);
            nestedScrollView.setNestedScrollingEnabled(false);

            recyclerViewGroceries = view.findViewById(R.id.groceries_recycler_vies);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Vendor.getAppContext());
            recyclerViewGroceries.setNestedScrollingEnabled(false);
            recyclerViewGroceries.setLayoutManager(layoutManager);


            dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.keepSynced(true);

            dbRef.child("banner").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    try {
                        final String imageurl = snapshot.getValue().toString();
                        //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                        Picasso.with(Vendor.getAppContext()).load(imageurl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(img_banner, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(Vendor.getAppContext()).load(imageurl).placeholder(R.drawable.banner).into(img_banner);
                            }
                        });
                    } catch (Exception e) {
                        img_banner.setImageResource(R.drawable.banner);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create Change Banner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try {

            SharedPreferences sh = this.getActivity().getSharedPreferences("MyLogin", MODE_PRIVATE);
            userId = sh.getString("userId", "");
            userType = sh.getString("userType", "");
            mProgress = new ProgressDialog(getContext());
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setTitle("Please wait....");


            loadAllProducts();

            btn_leafy_vegetables.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_leafy_vegetables.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_leafy_vegetables.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    showLeafyVegetable();
                }
            });

            btn_fruits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_fruits.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy_vegetables.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy_vegetables.setTextColor(getResources().getColor(R.color.textColorHint));


                    showFruits();
                }
            });

            btn_allProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_allProducts.setTextColor(Color.WHITE);

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy_vegetables.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy_vegetables.setTextColor(getResources().getColor(R.color.textColorHint));

                    loadAllProducts();

                }
            });


            btn_vege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_vege.setTextColor(Color.WHITE);

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy_vegetables.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy_vegetables.setTextColor(getResources().getColor(R.color.textColorHint));

                    showVegetables();
                }
            });

            btn_exo_vege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_exo_vege.setTextColor(Color.WHITE);

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy_vegetables.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy_vegetables.setTextColor(getResources().getColor(R.color.textColorHint));

                    showExoticVegetables();


                }
            });


        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;


    }

    private void loadAllProducts() {

        try {
            //show all products
            mProgress.setMessage("fetching all products");
            mProgress.show();

            productList = new ArrayList<>();
            productGuestList = new ArrayList<>();
            productList.clear();
            productGuestList.clear();
            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child("mart").orderByChild("productId").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.hasChildren()) {

                            for (DataSnapshot ds : snapshot.getChildren()) {

                                ContentData FruitProduct = ds.getValue(ContentData.class);
                                productList.add(FruitProduct);
                            }

                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(getContext(), "No Fruits", Toast.LENGTH_SHORT).show();
                        }


                        adapterProductUser = new AdapterProductUser(getContext(), productList);
                        adapterProductUser.notifyDataSetChanged();
                        recyclerViewGroceries.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Load All Products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void showFruits() {

        try {
            //show fruits
            mProgress.setMessage("fetching fruits");
            mProgress.show();
            productGuestList = new ArrayList<>();
            productList = new ArrayList<>();
            productList.clear();
            productGuestList.clear();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(true);
            {

                databaseReference.child("mart").child("4Fruit").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ContentData FruitProduct = ds.getValue(ContentData.class);
                                productList.add(FruitProduct);
                            }

                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(Vendor.getAppContext(), "No Fruits", Toast.LENGTH_SHORT).show();
                        }


                        adapterProductUser = new AdapterProductUser(getContext(), productList);
                        recyclerViewGroceries.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "load fruits: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void showVegetables() {

        try {
            //show vegetables
            mProgress.setMessage("fetching Vegetables");
            mProgress.show();
            productGuestList = new ArrayList<>();
            productList = new ArrayList<>();
            productList.clear();
            productGuestList.clear();
            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child("mart").child("1IndianVegetable").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ContentData VegetablesProduct = ds.getValue(ContentData.class);
                                productList.add(VegetablesProduct);
                            }

                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(Vendor.getAppContext(), "No Veggies", Toast.LENGTH_SHORT).show();
                        }


                        adapterProductUser = new AdapterProductUser(getContext(), productList);
                        recyclerViewGroceries.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Show Vegetable: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void showLeafyVegetable() {
        try {
            //show exotic
            mProgress.setMessage("fetching Leafy Vegetables");
            mProgress.show();
            productList = new ArrayList<>();
            productGuestList = new ArrayList<>();
            productGuestList.clear();
            productList.clear();

            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child("mart").child("2LeafyVegetable").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ContentData exotic_vegetables_Product = ds.getValue(ContentData.class);
                                productList.add(exotic_vegetables_Product);
                            }

                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(Vendor.getAppContext(), "No Products", Toast.LENGTH_SHORT).show();
                        }


                        adapterProductUser = new AdapterProductUser(getContext(), productList);
                        recyclerViewGroceries.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Exotic: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void showExoticVegetables() {

        try {
            //show exotic
            mProgress.setMessage("fetching Exotic Vegetables");
            mProgress.show();
            productList = new ArrayList<>();
            productGuestList = new ArrayList<>();
            productGuestList.clear();
            productList.clear();

            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.keepSynced(true);
                databaseReference.child("mart").child("3Exotic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ContentData exotic_vegetables_Product = ds.getValue(ContentData.class);
                                productList.add(exotic_vegetables_Product);
                            }

                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Toast.makeText(Vendor.getAppContext(), "No Products", Toast.LENGTH_SHORT).show();
                        }


                        adapterProductUser = new AdapterProductUser(getContext(), productList);
                        recyclerViewGroceries.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Exotic: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}



