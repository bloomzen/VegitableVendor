package com.example.vendor.AdminScreen;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveFragment extends Fragment {


    public RemoveFragment() {
        // Required empty public constructor
    }

    RelativeLayout rl_gold, rl_silver, rl_bronze, rl_fix;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remove, container, false);

        try {
            rl_bronze = view.findViewById(R.id.rl_bronzeRel_remove);
            rl_fix = view.findViewById(R.id.rl_fixedRel_remove);
            rl_gold = view.findViewById(R.id.rl_goldRel_remove);
            rl_silver = view.findViewById(R.id.rl_silverRel_remove);

            rl_silver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveHotelFragment hotelOrderFragment = new RemoveHotelFragment();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Silver");

                    hotelOrderFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                }
            });

            rl_gold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveHotelFragment hotelOrderFragment = new RemoveHotelFragment();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Gold");

                    hotelOrderFragment.setArguments(bundle);


                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                }
            });
            rl_fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveHotelFragment hotelOrderFragment = new RemoveHotelFragment();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Fixed");

                    hotelOrderFragment.setArguments(bundle);


                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                }
            });

            rl_bronze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveHotelFragment hotelOrderFragment = new RemoveHotelFragment();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Bronze");

                    hotelOrderFragment.setArguments(bundle);


                    fragmentTransaction.replace(R.id.fragment_Admin_container, hotelOrderFragment).addToBackStack(null).commit();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Remove: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

}
