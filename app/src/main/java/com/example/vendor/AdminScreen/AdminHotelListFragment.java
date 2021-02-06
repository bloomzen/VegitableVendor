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
import com.example.vendor.UserScreens.categoriesItemFragment;
import com.example.vendor.Vendor;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHotelListFragment extends Fragment {


    public AdminHotelListFragment() {
        // Required empty public constructor
    }





    RelativeLayout rl_gold, rl_silver, rl_bronze, rl_fix, rl_banner, rl_remove, rl_add, rl_updat_price, rl_guest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_hotel_list, container, false);

        try{
            rl_add = view.findViewById(R.id.rl_addHotel);
            rl_banner = view.findViewById(R.id.rl_changeBanner);
            rl_guest = view.findViewById(R.id.rl_guestRel);
            rl_bronze = view.findViewById(R.id.rl_bronzeRel);
            rl_fix = view.findViewById(R.id.rl_fixedRel);
            rl_gold = view.findViewById(R.id.rl_goldRel);
            rl_silver = view.findViewById(R.id.rl_silverRel);
            rl_remove = view.findViewById(R.id.rl_removeHotel);
            rl_updat_price = view.findViewById(R.id.rl_changePrices);

            rl_guest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HotelListFragment ldf = new HotelListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Guest");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_updat_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdatePriceListFragment ldf = new UpdatePriceListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_fix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelListFragment ldf = new HotelListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Fixed");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            rl_silver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelListFragment ldf = new HotelListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Silver");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_gold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelListFragment ldf = new HotelListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Gold");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });


            rl_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveFragment ldf = new RemoveFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });



            rl_bronze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HotelListFragment ldf = new HotelListFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","Bronze");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddBannerFragment ldf = new AddBannerFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddHotelFragment ldf = new AddHotelFragment();
                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_Admin_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Hotel List: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return view;
    }


}
