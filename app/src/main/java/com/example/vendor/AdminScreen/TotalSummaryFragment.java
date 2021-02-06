package com.example.vendor.AdminScreen;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.Adapters.AdapterProductUser;
import com.example.vendor.Adapters.AdapterTotalSummaryItemList;
import com.example.vendor.Adapters.SectionPagerAdapter;
import com.example.vendor.R;
import com.example.vendor.TotalSummaryConstructor;
import com.example.vendor.Vendor;
import com.example.vendor.orderItemData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalSummaryFragment extends Fragment {


    public TotalSummaryFragment() {
        // Required empty public constructor
    }

    TextView txt_tab_item_list, txt_tab_hotel_list;
    View view_item_list, view_hotel_list;

    RelativeLayout rl_view_item, rl_view_hotel;



    DatabaseReference mRef;

    RecyclerView recyclerView;




    ViewPager viewPager;
    TabLayout tabLayout;
    String currentDate,today;


    ArrayList<TotalSummaryConstructor> lists;
    AdapterTotalSummaryItemList adapter;


    ValueEventListener valueEventListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_summary, container, false);

        try {
            viewPager = view.findViewById(R.id.viewPagerTotal);
            tabLayout = view.findViewById(R.id.tabLayoutTotal);


//        recyclerView = view.findViewById(R.id.recycle__total_items);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMM.dd");
            currentDate = sdf.format(new Date());
            today = currentDate.replace(".","");


            mRef = FirebaseDatabase.getInstance().getReference().child("Total Order").child(today);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Total: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }








        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TotalItemsFragment(),"Item");
        adapter.addFragment(new HotelItemsFragment(),"Hotel");



        viewPager.setAdapter(adapter);




    }







    private void showItemListUI() {
        view_hotel_list.setVisibility(View.INVISIBLE);
        view_item_list.setVisibility(View.VISIBLE);
        rl_view_item.setVisibility(View.VISIBLE);
        rl_view_hotel.setVisibility(View.INVISIBLE);




    }

}
