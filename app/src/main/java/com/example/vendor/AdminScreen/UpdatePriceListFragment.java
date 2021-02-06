package com.example.vendor.AdminScreen;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterChangePrice;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatePriceListFragment extends Fragment {


    public UpdatePriceListFragment() {
        // Required empty public constructor
    }

    Button btn_allProducts, btn_vege, btn_fruits, btn_exo_vege, btn_leafy;
    RecyclerView rl_update_price_list;

    DatabaseReference mRef;
    ArrayList<ContentData> contentDataList;

    ProgressDialog mProgress;

    AdapterChangePrice adapter;
    String Vegetablekey, FruitKey, LeafyKey, ExoticKey, AllKey;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_price_list, container, false);

        try {
            btn_allProducts = view.findViewById(R.id.btn_allProducts_updatePrice);
            btn_vege = view.findViewById(R.id.btn_vegetables_updatePrice);
            btn_fruits = view.findViewById(R.id.btn_fruits_updatePrice);
            btn_exo_vege = view.findViewById(R.id.btn_exotic_fruits_updatePrice);
            btn_leafy = view.findViewById(R.id.btn_leafy_updatePrice);
            rl_update_price_list = view.findViewById(R.id.recycle_updatePriceList);
            rl_update_price_list.setLayoutManager(new LinearLayoutManager(getContext()));


            mRef = FirebaseDatabase.getInstance().getReference().child("mart");


            mProgress = new ProgressDialog(getContext());
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setTitle("Please wait....");


            showAllProducts();

            btn_allProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_allProducts.setTextColor(Color.WHITE);

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));


                    showAllProducts();

                }
            });

            btn_vege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_vege.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    showVege();
                }
            });

            btn_fruits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_fruits.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    showFruits();
                }
            });

            btn_exo_vege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_exo_vege.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_leafy.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_leafy.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));

                    showExco();
                }
            });

            btn_leafy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_leafy.setBackground(getResources().getDrawable(R.drawable.shape_explore));
                    btn_leafy.setTextColor(Color.WHITE);

                    btn_allProducts.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_allProducts.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    btn_fruits.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_fruits.setTextColor(getResources().getColor(R.color.textColorHint));


                    btn_exo_vege.setBackground(getResources().getDrawable(R.drawable.shape_explore01));
                    btn_exo_vege.setTextColor(getResources().getColor(R.color.textColorHint));

                    showLeafy();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "update: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    private void showAllProducts() {

        try {
            mProgress.setMessage("fetching all products");
            mProgress.show();
            contentDataList = new ArrayList<>();
            contentDataList.clear();

            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.hasChildren()) {

                        Toast.makeText(Vendor.getAppContext(), "allKey "+AllKey, Toast.LENGTH_SHORT).show();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData product = ds.getValue(ContentData.class);

                            Toast.makeText(Vendor.getAppContext(), "all "+AllKey, Toast.LENGTH_SHORT).show();
                            contentDataList.add(product);
                        }

                        mProgress.dismiss();
                    }else {
                        mProgress.dismiss();
                    }

                    adapter = new AdapterChangePrice(getContext(), contentDataList);
                    adapter.notifyDataSetChanged();
                    rl_update_price_list.setAdapter(adapter);
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

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "all: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showFruits(){
        try {
            mProgress.setMessage("fetching all products");
            mProgress.show();
            contentDataList = new ArrayList<>();
            contentDataList.clear();

            mRef.child("4Fruit").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        FruitKey = snapshot.getKey();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData product = ds.getValue(ContentData.class);
                            contentDataList.add(product);
                        }

                        mProgress.dismiss();
                    }else {
                        mProgress.dismiss();
                    }

                    adapter = new AdapterChangePrice(getContext(), contentDataList);
                    adapter.notifyDataSetChanged();
                    rl_update_price_list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Fruits: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }




    private void showLeafy(){
        try {
            mProgress.setMessage("fetching all products");
            mProgress.show();
            contentDataList = new ArrayList<>();
            contentDataList.clear();

            mRef.child("2LeafyVegetable").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        LeafyKey = snapshot.getKey();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData product = ds.getValue(ContentData.class);
                            contentDataList.add(product);
                        }

                        mProgress.dismiss();
                    }else {
                        mProgress.dismiss();
                    }

                    adapter = new AdapterChangePrice(getContext(), contentDataList);
                    adapter.notifyDataSetChanged();
                    rl_update_price_list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "leafy: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showVege(){
        try {
            mProgress.setMessage("fetching all products");
            mProgress.show();
            contentDataList = new ArrayList<>();
            contentDataList.clear();


            mRef.child("1IndianVegetable").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        Vegetablekey = snapshot.getKey();
                        Log.v("key",Vegetablekey);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData product = ds.getValue(ContentData.class);
                            contentDataList.add(product);
                        }

                        mProgress.dismiss();
                    }else {
                        mProgress.dismiss();
                    }

                    adapter = new AdapterChangePrice(getContext(), contentDataList);
                    adapter.notifyDataSetChanged();
                    rl_update_price_list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "vegetable: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void showExco(){
        try {
            mProgress.setMessage("fetching all products");
            mProgress.show();
            contentDataList = new ArrayList<>();
            contentDataList.clear();

            mRef.child("3Exotic").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {

                        ExoticKey = snapshot.getKey();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData product = ds.getValue(ContentData.class);
                            contentDataList.add(product);
                        }

                        mProgress.dismiss();
                    }else {
                        mProgress.dismiss();
                    }

                    adapter = new AdapterChangePrice(getContext(), contentDataList);
                    adapter.notifyDataSetChanged();
                    rl_update_price_list.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Exotic: "+e.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }

    }

}
