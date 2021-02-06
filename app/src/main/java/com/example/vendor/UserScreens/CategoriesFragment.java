package com.example.vendor.UserScreens;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {


    public CategoriesFragment() {
        // Required empty public constructor
    }

    LinearLayout linearFruits, linearIndian, linearExotic, linearLeafy;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categories, container, false);

        try {
            linearExotic = view.findViewById(R.id.linearExotic);
            linearFruits = view.findViewById(R.id.linearFruits);
            linearLeafy = view.findViewById(R.id.linearLeafy);
            linearIndian = view.findViewById(R.id.linearIndian);


            //takes to fragment to show indian data
            linearIndian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoriesItemFragment ldf = new categoriesItemFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","1IndianVegetable");

                    ldf.setArguments(bundle);

                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            //takes to fragment to show fruits data
            linearFruits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoriesItemFragment ldf = new categoriesItemFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","4Fruit");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });




            //takes to fragment to show leafy data
            linearLeafy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoriesItemFragment ldf = new categoriesItemFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","2LeafyVegetable");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            //takes to fragment to show exotic data
            linearExotic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoriesItemFragment ldf = new categoriesItemFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();


                    Bundle bundle=new Bundle();
                    bundle.putString("categoriesName","3Exotic");

                    ldf.setArguments(bundle);
                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create View: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return view;
    }


    //fragmentManager.popBackStack()

}
