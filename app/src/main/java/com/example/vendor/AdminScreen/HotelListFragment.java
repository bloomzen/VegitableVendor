package com.example.vendor.AdminScreen;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.HolderHotelName;
import com.example.vendor.HotelNameContsnats;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelListFragment extends Fragment {


    public HotelListFragment() {
        // Required empty public constructor
    }

    RecyclerView rlHotelList;
    RelativeLayout rl_add, rl_remove;
    String category;
    TextView txt_hotelCat;

    DatabaseReference dbRef;

    ArrayList<HotelNameContsnats> hotelLists;

    FirebaseRecyclerAdapter<HotelNameContsnats, HolderHotelName> adapters;
    FirebaseRecyclerOptions<HotelNameContsnats> options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);

        try {

            Bundle bundle = getArguments();
            final String cate = bundle.getString("categoriesName");
            Toast.makeText(getContext(), "catw", Toast.LENGTH_SHORT).show();
            rlHotelList = view.findViewById(R.id.hotelNameRecycler);
            rlHotelList.setLayoutManager(new LinearLayoutManager(getContext()));
            rl_add = view.findViewById(R.id.rl_Category_addHotel);
            rl_remove = view.findViewById(R.id.rl_category_removeHotel);
            txt_hotelCat = view.findViewById(R.id.txt_AdminHotel);

            txt_hotelCat.setText("" + cate + " Hotel List");

            dbRef = FirebaseDatabase.getInstance().getReference().child("HotelName").child(cate);
            Query query = dbRef;
            options = new FirebaseRecyclerOptions.Builder<HotelNameContsnats>().setQuery(query, HotelNameContsnats.class).setLifecycleOwner(this).build();


            adapters = new FirebaseRecyclerAdapter<HotelNameContsnats, HolderHotelName>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final HolderHotelName holder, int position, @NonNull final HotelNameContsnats model) {
                    // Toast.makeText(getContext(), model.getHotelName(), Toast.LENGTH_SHORT).show();
                    holder.txt_HotelName.setText("" + model.getHotelName());
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AdminAccountListFragment hotelOrderFragment = new AdminAccountListFragment();

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                            SharedPreferences sh = Vendor.getAppContext().getSharedPreferences("MyHotelDetails", MODE_PRIVATE);


                            SharedPreferences.Editor myEdit = sh.edit();
                            myEdit.putString("memberShip", cate);
                            myEdit.putString("HotelName",model.getHotelName());
                            myEdit.apply();

                            fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                        }
                    });

                }

                @NonNull
                @Override
                public HolderHotelName onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.hotel_list, parent, false);

                    return new HolderHotelName(view1);
                }
            };


            rlHotelList.setAdapter(adapters);
        } catch (Exception e) {
            Toast.makeText(Vendor.getAppContext(), "On Create" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapters.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapters.startListening();
    }


}
