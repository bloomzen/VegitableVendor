package com.example.vendor.AdminScreen;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vendor.Adapters.SectionPagerAdapter;
import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminOrdersFragment extends Fragment {


    public AdminOrdersFragment() {
        // Required empty public constructor
    }

    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        try {
            viewPager = view.findViewById(R.id.viewPager);
            tabLayout = view.findViewById(R.id.tabLayout);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Tab: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            setUpViewPager(viewPager);

            tabLayout.setupWithViewPager(viewPager);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Activity: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpViewPager(ViewPager viewPager) {
        try{
            SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
            adapter.addFragment(new orderPendingFragment(),"Pending");
            adapter.addFragment(new OrderApprovalFragment(),"Approve");
            adapter.addFragment(new OrderCompletedFragment(),"Completed");


            viewPager.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "setYpViewPager: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }





}
