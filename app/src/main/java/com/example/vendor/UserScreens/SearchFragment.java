package com.example.vendor.UserScreens;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterGuestUser;
import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.ContentData;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.example.vendor.gusteContentData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    boolean isThere;

    EditText edt_search;
    View view;
    CircleImageView img_search_btn;
    CharSequence searchTxt;

    ArrayList<ContentData> productList;
    AdapterProductUser adapterProductUser;

    ArrayList<gusteContentData> productListGuest;
    AdapterGuestUser adapterProductUserGuest;
    RecyclerView recyclerViewGroceries;
    ImageButton btn_back;
    String userId, userType;

    ImageView img_no_item;
    TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        try {
            edt_search = view.findViewById(R.id.edt_search);
            img_search_btn = view.findViewById(R.id.img_btn_search);
            recyclerViewGroceries = view.findViewById(R.id.recycler_search);
            recyclerViewGroceries.setLayoutManager(new LinearLayoutManager(getContext()));

            SharedPreferences sh = this.getActivity().getSharedPreferences("MyLogin", MODE_PRIVATE);
            userId = sh.getString("userId", "");
            userType = sh.getString("userType", "");

            btn_back = view.findViewById(R.id.btn_search_back);
            txt = view.findViewById(R.id.txt);
            img_no_item = view.findViewById(R.id.img_no_item);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeFragment ldf = new HomeFragment();

                    FragmentTransaction transection = getFragmentManager().beginTransaction();


                    transection.replace(R.id.fragment_container, ldf);
                    transection.commit();
                }
            });


            loadAllProducts();

            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    try {
                        searchTxt = s;
                        search(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        searchTxt = s;
                        search(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            img_search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edt_search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            try {
                                searchTxt = s;
                                search(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                searchTxt = s;
                                search(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            });
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;

    }

    private void search(CharSequence s) {

        try {
            ArrayList<ContentData> myList = new ArrayList<>();
            for (ContentData object : productList) {
                if (object.getItemName().toLowerCase().contains(s)) {
                    myList.add(object);
                    isThere = true;

                    txt.setVisibility(View.INVISIBLE);
                    img_no_item.setVisibility(View.INVISIBLE);
                }
            }

            if(!isThere){
                txt.setVisibility(View.VISIBLE);
                img_no_item.setVisibility(View.VISIBLE);
            }


            adapterProductUser = new AdapterProductUser(getContext(), myList);
            recyclerViewGroceries.setAdapter(adapterProductUser);




        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Search: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void loadAllProducts() {


        try {
            productList = new ArrayList<>();
            productListGuest = new ArrayList<>();
            productList.clear();
            productListGuest.clear();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.child("mart").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ContentData FruitProduct = ds.getValue(ContentData.class);
                            productList.add(FruitProduct);
                        }


                    } else {

                        Toast.makeText(Vendor.getAppContext(), "No Products", Toast.LENGTH_SHORT).show();
                    }


                    adapterProductUser = new AdapterProductUser(getContext(), productList);
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

        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "Load Products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
