package com.example.vendor.UserScreens;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterGuestUser;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.gusteContentData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class categoriesItemFragment extends Fragment {


    public categoriesItemFragment() {
        // Required empty public constructor
    }

    View view;
    TextView txt_title;
    RecyclerView recycler_item_categories;

    ArrayList<ContentData> productList;
    AdapterProductUser adapterProductUser;

    ArrayList<gusteContentData> productListGuest;
    AdapterGuestUser adapterProductUserGuest;

    String userId, userType;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories_item, container, false);


        try{
            txt_title = view.findViewById(R.id.categoriesTitle);

            SharedPreferences sh =this.getActivity().getSharedPreferences("MyLogin",MODE_PRIVATE);
            userId = sh.getString("userId","");
            userType = sh.getString("userType","");
            Bundle bundle=getArguments();
            String cate = bundle.getString("categoriesName");
            if(cate.equals("1IndianVegetable")){
                txt_title.setText("Indian Vegetables");

            }else if(cate.equals("2LeafyVegetable")) {
                txt_title.setText("Leafy Vegetable");
            }else if(cate.equals("4Fruit")){
                txt_title.setText("Fruit");
            }else if(cate.equals("3Exotic")){
                txt_title.setText("Exotic");
            }

            recycler_item_categories = view.findViewById(R.id.groceries_recycler_vies_categories_item);
            recycler_item_categories.setLayoutManager(new LinearLayoutManager(getContext()));

            getProduct(cate);

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }




        return view;
    }

    public void getProduct(String categoies){
        try{
            productList = new ArrayList<>();
            productListGuest = new ArrayList<>();
            productListGuest.clear();
            productList.clear();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("mart").child(categoies).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChildren()){
                        for(DataSnapshot ds : snapshot.getChildren()){
                            ContentData FruitProduct = ds.getValue(ContentData.class);
                            productList.add(FruitProduct);
                        }


                    }else {

                        Toast.makeText(Vendor.getAppContext(), "No Products", Toast.LENGTH_SHORT).show();
                    }



                    adapterProductUser = new AdapterProductUser(getContext(), productList);
                    recycler_item_categories.setAdapter(adapterProductUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "load Products: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


}
